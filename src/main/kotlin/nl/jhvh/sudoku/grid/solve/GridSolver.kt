package nl.jhvh.sudoku.grid.solve

import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.format.SudokuFormatter
import nl.jhvh.sudoku.grid.defaultGridFormatter
import nl.jhvh.sudoku.grid.event.ValueEvent
import nl.jhvh.sudoku.grid.event.ValueEventListener
import nl.jhvh.sudoku.grid.event.ValueEventType
import nl.jhvh.sudoku.grid.event.ValueEventType.SET_CELL_VALUE
import nl.jhvh.sudoku.grid.event.cellvalue.CellRemoveCandidatesEvent
import nl.jhvh.sudoku.grid.event.cellvalue.SetCellValueEvent
import nl.jhvh.sudoku.grid.gridWithCandidatesBoxFormatter
import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.grid.model.GridElement
import nl.jhvh.sudoku.grid.model.cell.Cell
import nl.jhvh.sudoku.grid.model.cell.CellValue
import nl.jhvh.sudoku.grid.model.cell.CellValue.FixedValue
import nl.jhvh.sudoku.grid.model.cell.CellValue.NonFixedValue
import nl.jhvh.sudoku.grid.model.segment.Block
import nl.jhvh.sudoku.grid.model.segment.Col
import nl.jhvh.sudoku.grid.model.segment.GridSegment
import nl.jhvh.sudoku.grid.model.segment.LinearSegment
import nl.jhvh.sudoku.grid.model.segment.Row
import nl.jhvh.sudoku.grid.solve.GridSolver.GridSolvingPhase.CHAINING_TECHNIQUE
import nl.jhvh.sudoku.grid.solve.GridSolver.GridSolvingPhase.Companion.firstSolvingPhase
import nl.jhvh.sudoku.grid.solve.GridSolver.GridSolvingPhase.GRID_SOLVED
import nl.jhvh.sudoku.grid.solve.GridSolver.GridSolvingPhase.MULTI_SEGMENT_COMBINATIONS
import nl.jhvh.sudoku.grid.solve.GridSolver.GridSolvingPhase.NOT_STARTED
import nl.jhvh.sudoku.grid.solve.GridSolver.GridSolvingPhase.PREPARED_FOR_SOLVING
import nl.jhvh.sudoku.grid.solve.GridSolver.GridSolvingPhase.SOLVE_EXCLUDING_COMBINATIONS
import nl.jhvh.sudoku.grid.solve.GridSolver.GridSolvingPhase.SOLVE_POLYMORPHIC_COMBINATIONS
import nl.jhvh.sudoku.grid.solve.GridSolver.GridSolvingPhase.SOLVE_SINGLE_CANDIDATE_VALUES
import nl.jhvh.sudoku.grid.solve.GridSolver.SubSegment.HorizontalSubSegment
import nl.jhvh.sudoku.grid.solve.GridSolver.SubSegment.VerticalSubSegment
import nl.jhvh.sudoku.util.allCombinations
import nl.jhvh.sudoku.util.checkAndLog
import nl.jhvh.sudoku.util.concatEach
import nl.jhvh.sudoku.util.log
import nl.jhvh.sudoku.util.requireAndLog
import java.util.*
import java.util.Collections.unmodifiableMap
import java.util.Collections.unmodifiableSet
import java.util.concurrent.LinkedBlockingQueue
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.HashSet
import kotlin.collections.LinkedHashSet
import java.util.concurrent.ConcurrentHashMap.newKeySet as ConcurrentHashSet

/**
 * Stateful solver for the [gridToSolve]. The class is deliberately defined as open, to allow extension.
 *  * The [gridToSolve] must be set before any further use
 *  * The [gridToSolve] can be set only once
 */
open class GridSolver: GridSolvable, ValueEventListener, ValueEventHandlable {

    override var gridToSolve: Grid? = null
        set(grid) {
            requireAndLog(grid != null) {
                "The $gridToSolvePropertyName can be set to a non-null ${Grid::class.simpleName} value only, but is set to `null`!!"
            }
            if (field === grid) {
                log().debug { "This $gridToSolvePropertyName was set already to the same ${Grid::class.simpleName}:" +
                        "$gridToSolvePropertyName=${grid!!.toStringCompact()}" }
                return
            }
            checkAndLog (field == null)  {
                "A $gridToSolvePropertyName was already set for this ${this.javaClass.simpleName}! It can be set only once, the new value is rejected!" +
                        " Existing value of $gridToSolvePropertyName = ${field!!.toStringCompact()}; new value = ${grid!!.toStringCompact()}"
            }
            field = grid!!
            prepareForSolving()
        }

    // for internal use, not nullable, so we need not use `gridToSolve!!` everywhere
    private val grid: Grid
        get() =  gridToSolve ?: throw GridNotSetException(
            "$gridToSolvePropertyName must be set before further usage of ${GridSolver::class.simpleName}")

    @Volatile
    var solvingPhase: GridSolvingPhase = NOT_STARTED
        protected set

    override val isSolving
        get() = solvingPhase.isSolving

    private var isSolvable = true

    override val unSolvable
        get() = if (!isSolvable) !isSolvable else null

    protected val eventQueue = LinkedBlockingQueue<ValueEvent>()

    protected val segments: Set<GridSegment> by lazy { with(grid) { (this.rowList + this.colList + this.blockList).toSet() } }

    protected val horizontalSubSegments: Set<HorizontalSubSegment> by lazy {
        val subSegmentSet: MutableSet<HorizontalSubSegment> = HashSet(grid.gridSize)
                (0 until grid.gridSize).forEach { rowIndex ->
                    (0 until grid.gridSize step grid.blockSize).forEach {leftColIndex ->
                        subSegmentSet.add(HorizontalSubSegment(grid, rowIndex, leftColIndex))
                    }
                }
        unmodifiableSet(subSegmentSet)
    }

    protected val verticalSubSegments: Set<VerticalSubSegment> by lazy {
        val subSegmentSet: MutableSet<VerticalSubSegment> = HashSet(grid.gridSize)
        (0 until grid.gridSize).forEach { colIndex ->
            (0 until grid.gridSize step grid.blockSize).forEach {topRowIndex ->
                subSegmentSet.add(VerticalSubSegment(grid, colIndex, topRowIndex))
            }
        }
        unmodifiableSet(subSegmentSet)
    }

    protected val horizontalSubSegmentsByRow: Map<Row, Set<HorizontalSubSegment>> by lazy {
        val subSegmentsByRowMap =
                horizontalSubSegments
                        .groupBy{ it.enclosingLinearSegment }
                        .mapValues { it -> it.value.toSet() }
        unmodifiableMap(subSegmentsByRowMap)
    }

    protected val verticalSubSegmentsByCol: Map<Col, Set<VerticalSubSegment>> by lazy {
        val subSegmentsByColMap =
                verticalSubSegments
                        .groupBy{ it.enclosingLinearSegment }
                        .mapValues { it -> it.value.toSet() }
        unmodifiableMap(subSegmentsByColMap)
    }

    protected val subSegmentsByLinearSegment: Map<LinearSegment, Set<SubSegment<LinearSegment>>> by lazy {
        val subSegmentsByLinearSegment: MutableMap<LinearSegment, Set<SubSegment<LinearSegment>>>
                = HashMap(horizontalSubSegmentsByRow.size + verticalSubSegmentsByCol.size)
        subSegmentsByLinearSegment.putAll(horizontalSubSegmentsByRow)
        subSegmentsByLinearSegment.putAll(verticalSubSegmentsByCol)
        subSegmentsByLinearSegment
    }

    protected val horizontalSubSegmentsByBlock: Map<Block, Set<HorizontalSubSegment>> by lazy {
        val subSegmentsByBlockMap =
                horizontalSubSegments
                        .groupBy{ it.enclosingBlock }
                        .mapValues { it -> it.value.toSet() }
        unmodifiableMap(subSegmentsByBlockMap)
    }

    protected val verticalSubSegmentsByBlock: Map<Block, Set<VerticalSubSegment>> by lazy {
        val subSegmentsByBlockMap =
                verticalSubSegments
                        .groupBy{ it.enclosingBlock }
                        .mapValues { it -> it.value.toSet() }
        unmodifiableMap(subSegmentsByBlockMap)
    }

    protected val segmentsByCellValue: Map<CellValue, Set<GridSegment>> by lazy {
        val segsByCellValue: MutableMap<CellValue, MutableSet<GridSegment>> = HashMap(grid.cellList.size)
        segments.forEach { segment ->
            segment.cells.forEach { cell ->
                // initialCapacity = 3: one for each row, column and block a cell is part of
                segsByCellValue.putIfAbsent(cell.cellValue, HashSet(3))
                segsByCellValue[cell.cellValue]!!.add(segment)
            }
        }
        unmodifiableMap(segsByCellValue)
    }

    protected val unSolvedNumbersBySegment: Map<GridSegment, MutableSet<Int>> by lazy {
        val cellValsBySegment: MutableMap<GridSegment, MutableSet<Int>> = HashMap(segments.size)
        segments.forEach { segment ->
            val unsolvedNumbers = ConcurrentHashSet<Int>(grid.gridSize)
            unsolvedNumbers.addAll((1..grid.gridSize) -
                    segment.cells
                    .filter { cell -> cell.isSet }
                    .map { cell -> cell.cellValue.value!! }
            )
            cellValsBySegment.put(segment, unsolvedNumbers)
        }
        unmodifiableMap(cellValsBySegment)
    }


    protected val cellValuesBySegment: Map<GridSegment, Set<CellValue>> by lazy {
        val cellValsBySegment: MutableMap<GridSegment, Set<CellValue>> = HashMap(segments.size)
        segments.forEach { segment ->
            val cellValues = HashSet<CellValue>(grid.gridSize)
            cellValues.addAll(segment.cells
                    .map { cell -> cell.cellValue }
            )
            cellValsBySegment.put(segment, cellValues)
        }
        unmodifiableMap(cellValsBySegment)
    }

    protected val nonFixedBySegment: Map<GridSegment, Set<NonFixedValue>> by lazy {
        val cellValsBySegment: MutableMap<GridSegment, Set<NonFixedValue>> = HashMap(segments.size)
        segments.forEach { segment ->
            val nonFixed = HashSet<NonFixedValue>(grid.gridSize)
            nonFixed.addAll(segment.cells
                    .map { cell -> cell.cellValue }
                    .filter { it is NonFixedValue}
                    .map { it as NonFixedValue }
            )
            cellValsBySegment.put(segment, nonFixed)
        }
        unmodifiableMap(cellValsBySegment)
    }

    /**
     * Unsolved values, initialized on first access.
     * After 1st initialization, values are to be removed by the solving algorithm when solved
     */
    private val unSolvedNonFixedValues: MutableSet<NonFixedValue> by lazy {
        val map = grid.cellList
                .map { cell -> cell.cellValue }
                .filter { cellValue -> !cellValue.isFixed }
                .map { it as NonFixedValue }
        val concurrentHashSet = ConcurrentHashSet<NonFixedValue>(map.size)
        concurrentHashSet.addAll(map)
        concurrentHashSet
    }

    fun getUnSolvedCellValues(): Set<NonFixedValue> = unmodifiableSet(unSolvedNonFixedValues)

    protected fun subscribeToEvents(excludeFixed: Boolean = true, vararg valueEventTypes: ValueEventType = ValueEventType.values()) {
        valueEventTypes.forEach { eventType ->
            grid.cellList
                    .map { it.cellValue }
                    .filter { !excludeFixed || !it.isFixed }
                    .forEach { cellValue -> cellValue.subscribe(this, eventType) }
        }
    }

    override fun handleSetCellValueEvent(event: SetCellValueEvent) {
        log().trace { "$event handled by ${GridSolver::class.simpleName}" }
        val eventSource = event.eventSource
        unSolvedNonFixedValues.remove(eventSource)
        segmentsByCellValue[eventSource]!!.forEach {
            unSolvedNumbersBySegment[it]!!.
            remove(
                    eventSource.value!!)
        }
        // Once set, the cellValue should not produce any events anymore, so we are not interested anymore: unsubscribe
        eventSource.unsubscribe(this, event.type)
        val eventSourceSegments: Set<GridSegment> = segmentsByCellValue[eventSource]!!
        if (eventSource is NonFixedValue) {
            eventSource.clearValueCandidatesOnValueSet()
        }
        eventSourceSegments.forEach { gridSegment ->
                    gridSegment.cells.forEach { cell ->
                        if (cell.cellValue !== eventSource) {
                            if (cell.cellValue is NonFixedValue) {
                                // For cells that are in the same Block AND in either the same Row or same Col
                                // as the eventSource, method removeValueCandidate will be called more than once.
                                // Making cells unique beforehand would probably be more costly (in terms of memory
                                // and/or performance) than just calling this method more than once.
                                // Method removeValueCandidate should be lenient for this, and ignore redundant calls.
                                cell.cellValue.removeValueCandidate(event.newValue)
                            }
                        }
                    }
                }
    }

    override fun handleRemoveCandidatesEvent(event: CellRemoveCandidatesEvent) {
        log().trace { "$event handled by ${GridSolver::class.simpleName}" }
        if (event.eventSource.getValueCandidates().size <= 1) {
            // either the cell will be solved (value will be set), or there is no solution.
            // in both cases we want to unsubscribe
            event.eventSource.unsubscribe(this, event.type)
        }
    }

    protected fun prepareForSolving() {
        if (solvingPhase >= PREPARED_FOR_SOLVING) {
            log().warn { "Grid was already prepared for solving - call to prepare it again is ignored" }
        }
        else {
            subscribeToEvents(excludeFixed = false, SET_CELL_VALUE)
            grid.cellList
                    .map { it.cellValue }
                    .filter { it.isFixed }
                    .map { it as FixedValue }
                    .forEach { fixedValue ->
                        // Publishing here causes initial elimination of impossible candidate values.
                        // E.g. a fixed value of 4 will remove all 4s in its row / column / block.
                        // This is done before this GridSolver has subscribed to [CellRemoveCandidateEvent]s,
                        // so it should have no side effects like preliminarily starting to solve the Sudoku
                        fixedValue.publish(SetCellValueEvent(eventSource = fixedValue, newValue = fixedValue.value!!))
                    }
            solvingPhase = PREPARED_FOR_SOLVING
        }
    }

    private fun assertCorrectGrid() {
        segments.forEach { segment ->
            val valueList = segment.cells.filter { cell -> cell.isSet }.map { cell -> cell.cellValue.value!! }
            val valueSet = valueList.toSet()
            if (valueList.size > valueSet.size) {
                val formattedSegment = segment.format(defaultGridFormatter).toString()
                throw AssertionError("Duplicate value in $grid\n\n in $segment:\n$formattedSegment\n")
            }
        }
    }

    /**
     * If a cell has `[5]` as it's only remaining candidate value, no other cells in the [GridSegment]s the cell is part of
     * can have that value, so `[5]` can be eliminated from the other cells.
     */
    protected fun solveSingularCandidates(nonFixedValue: NonFixedValue) {
//        synchronized(grid) {
            val valueCandidates = nonFixedValue.getValueCandidates()
            when (valueCandidates.size) {
                0 -> {
                    if (!nonFixedValue.isSet) {
                        throw GridNotSolvableException("Grid is not solvable: value not set, and no candidate values anymore in $nonFixedValue! cellValue=$nonFixedValue")
                    }
                }
                1 -> {
                    // allow null because the valueCandidates might be cleared by a concurrent thread / coroutine
                    val intValue = valueCandidates.firstOrNull()
                    if (intValue != null && !nonFixedValue.isSet) {
                        nonFixedValue.setValue(intValue)
                    }
                }
            }
//        }
//        handleEvents()
assertCorrectGrid()
    }

    /**
     * If a cell has `[5]` as it's only remaining candidate value, no other cells in the [GridSegment]s the cell is part of
     * can have that value, so `[5]` can be eliminated from the other cells.
     */
    fun solveSingularCandidates() {
        unSolvedNonFixedValues
                .filter { it.getValueCandidates().size <= 1}
                .forEach { nonFixedValue -> solveSingularCandidates(nonFixedValue) }
    }

    /**
     * If cells in a [GridSegment] have different combinations of values,
     * e.g. `[1, 3, 6]`, `[2, 3, 7]`, `[1, 3, 7, 9]`, `[2, 4, 5, 8, 9]`
     * and only 1 of those has `[6]` as a candidate value, that cell's value can be set to `[6]`.
     */
    fun solveSingleValueInSegments() {
//        synchronized(grid) {
            var segmentCellValuesWithCandidate: List<CellValue>
            segments.forEach { segment ->
                unSolvedNumbersBySegment[segment]!!.forEach { value ->
                    val segmentCellValues = cellValuesBySegment[segment]!!
                    segmentCellValuesWithCandidate = segmentCellValues
                            .filter { it is NonFixedValue && it.getValueCandidates().contains(value) }
                    if (segmentCellValuesWithCandidate.size == 1) {
                        segmentCellValuesWithCandidate[0].setValue(value)
                    }
                }
            }
//        }
//        handleEvents()
assertCorrectGrid()
    }

    /**
     * if in a [Block] a combination of values, say, `[2, 6]`, only exists in a single row part ([SubSegment]) of the [Block],
     * (so these all have, say, [Block.rowIndex] = 4, thus the cells with [Block.rowIndex] 4 have candidates `[2, 4, 7]`, `[2, 6, 7]`
     * and `[3, 6]`, and no other cells in that block have candidate `[2]` or `[6]`), than these values 2 and 6 can be removed from
     * all other cells in the [Row] with [Row.rowIndex] = 4.
     */
    protected fun solveExcludingSubsetCandidates(subSegment: SubSegment<LinearSegment>, otherBlockCandidates: Set<Int>) {
//        synchronized(grid) {
            val subSegmentCellValuesByCandidates = subSegment.cells
                    .filter { !it.isFixed && !it.isSet }
                    .map { cell -> cell.cellValue as NonFixedValue }
                    .groupBy { nonFixedValue -> nonFixedValue.getValueCandidates() }
            val subSegmentCandidateValues = subSegmentCellValuesByCandidates.keys.flatten().toSet()
            val remainingCandidateValues = (subSegmentCandidateValues - otherBlockCandidates).toIntArray()
            if (remainingCandidateValues.isNotEmpty()) {
                // we now have candidate values that exist in the current subSegment, but not anywhere else in the Block
                // so these can be removed:
                //  1. from the candidate values in the same linear segment (Row or Col) that are not part of this Block
                val otherCellsOfLinearSegment = subSegment.enclosingLinearSegment.cells.filter { !it.isFixed } - subSegment.cells
                otherCellsOfLinearSegment.map { cell -> cell.cellValue as NonFixedValue }.forEach { nonFixedValue ->
                    nonFixedValue.removeValueCandidate(*remainingCandidateValues)
                }
                //  2. from the candidate values in the sub segments of the Block that are not part of that linear segment
                val otherCellsOfBlock = subSegment.enclosingBlock.cells.filter { !it.isFixed } - subSegment.cells
                otherCellsOfBlock.map { cell -> cell.cellValue as NonFixedValue }.forEach { nonFixedValue ->
                    nonFixedValue.removeValueCandidate(*remainingCandidateValues)
                }
            }
//        }
assertCorrectGrid()
    }

    /**
     * if in a [Block] a combination of values, say, `[2, 6]`, only exists in a single row part ([SubSegment]) of the [Block],
     * (so these all have, say, [Row.rowIndex] = 4, and the [Block] cells with [Row.rowIndex] 4 have candidates `[2, 4, 7]`, `[2, 6, 7]`
     * and `[3, 6]`, and no other cells in that block have candidate `[2]` or `[6]`), than these values 2 and 6 can be removed from
     * all other [SubSegment]s of the [Row] with [Row.rowIndex] = 4.
     */
    private fun solveExcludingSubsetCandidates(linearSegmentsByBlock: Map<Block, Set<SubSegment<LinearSegment>>>, currentBlock: Block) {
        linearSegmentsByBlock[currentBlock]!!.forEach { subSegmentOfBlock ->
            val otherSubSegmentsOfBlock = linearSegmentsByBlock[currentBlock]!!.minus(subSegmentOfBlock)
            val otherBlockCandidates = otherSubSegmentsOfBlock
                    .map { subSegment -> subSegment.cells }
                    .flatten()
                    .filter { cell -> !cell.isFixed }
                    .map { cell -> (cell.cellValue as NonFixedValue).getValueCandidates() }
                    .flatten()
                    .toSet()
                    .union(otherSubSegmentsOfBlock
                            .map { horizontalSubSegment -> horizontalSubSegment.cells }
                            .flatten()
                            .filter { cell -> cell.isSet }
                            .map { cell -> cell.cellValue.value!! }
                    )
            solveExcludingSubsetCandidates(subSegmentOfBlock, otherBlockCandidates)
        }
    }

    /**
     * if in a [Block] a combination of values, say, `[2, 6]`, only exists in a single row part ([SubSegment]) of the [Block],
     * (so these all have, say, [Row.rowIndex] = 4, and the [Block] cells with [Row.rowIndex] 4 have candidates `[2, 4, 7]`, `[2, 6, 7]`
     * and `[3, 6]`, and no other cells in that block have candidate `[2]` or `[6]`), than these values 2 and 6 can be removed from
     * all other [SubSegment]s of the [Row] with [Row.rowIndex] = 4.
     */
    fun solveExcludingSubsetCandidates() {
//        synchronized(grid) {

            grid.blockList.forEach { block ->
                solveExcludingSubsetCandidates(horizontalSubSegmentsByBlock, block)
                solveExcludingSubsetCandidates(verticalSubSegmentsByBlock, block)
            }
//        }
//        handleEvents()
    }

    /**
     *  * if 2 cells in a [GridSegment] both have `[2, 4]` as candidates, no other cells in that segment can have these
     *    candidate values, so these can be eliminated from the other [CellValue]s in the [GridSegment]
     */
    private fun solveIdenticalCandidateSets() {
//        synchronized(grid) {
            nonFixedBySegment.forEach { segment, segmentCellValues ->
                val nonFixedByCandidates: Map<Set<Int>, List<NonFixedValue>> =
                        segmentCellValues
                                .groupBy { it.getValueCandidates() }
                                // Only those where more than 1 cellValue with identical candidates
                                .filter { it.value.size > 1 }
                nonFixedByCandidates.forEach { candidates, cellValuesWithSameCandidates ->
                    // if 2 identical candidates (say, [3, 8]) occur in 2 cells,
                    // or 3 identical candidates (say, [2, 5, 7]) occur in 3 cells, etc.,
                    // these values can not appear in the other cells in the same segment,
                    // so these can be eliminated from the candidates of the other cells in the segment
                    if (candidates.size == cellValuesWithSameCandidates.size) {
                        val targetCells = segmentCellValues - cellValuesWithSameCandidates
                        targetCells.forEach { nonFixedValue ->
                            nonFixedValue.removeValueCandidate(*candidates.toIntArray())
                        }
                    }
                }
            }
//        }
//        handleEvents()
//assertCorrectGrid()
    }

    /**
     *  * if 3 cells in a [GridSegment] have candidates, say, `[2, 8]`, `[3, 8]`, `[2, 8]`, then the values `[2, 3, 8]` can be removed
     *    from the candidates of all other cells in the same [GridSegment].
     *  * if 4 cells have candidates `[1, 2]`, `[1, 2, 3]`, `[2, 3]`, `[1, 4]`, then the values `[1, 2, 3, 4]` can be removed
     *    from the candidates of all other cells in the same [GridSegment].
     */
    fun solvePolymorphicExclusions() {
        // gridSize divided by 2: In a grid of nine you may have excluding combinations of 1..8.
        // But if you have a combination of, say [1, 4, 5, 6, 7, 9], then there must also be a smaller combination,
        // in this case [1, 3, 8]. So it makes no sense to look for combinations of gridSize / 2 or larger, finding
        // larger combinations is much more costly in terms of computing power & memory
        var goNextCombinationSize = true
        (1..(grid.gridSize/2)).forEach combiLengthLoop@ { combinationLength ->
            if (!goNextCombinationSize) {
                // If we have found combinations of, say, 3, we do not proceed with combinations of 4,
                // we better handle events first (with less complicated solution methods)
                return
            }
            segments.forEach segmentLoop@ { segment ->
                // find all candidate values of unsolved cells of the segment that have at most `combinationLength` candidates
                val targetCandidates: SortedSet<Int> = cellValuesBySegment[segment]!!
                        .filter { cellValue -> !cellValue.isFixed && !cellValue.isSet }
                        .map { cellValue -> (cellValue as NonFixedValue).getValueCandidates()}
                        .filter { valueCandidates -> valueCandidates.size <= combinationLength }
                        .flatten()
                        .toSortedSet()
                if (targetCandidates.isEmpty()) {
                    return@segmentLoop
                }
                // find the cells in the segment whose candidates are equal to or a subset of the targetCandidates
                val targetUnsolved: List<NonFixedValue> = cellValuesBySegment[segment]!!
                        .filter { cellValue -> !cellValue.isFixed &&!cellValue.isSet }
                        .map { cellValue -> (cellValue as NonFixedValue)}
                        .filter { nonFixedValue ->
                            nonFixedValue.getValueCandidates().size <= combinationLength
                                    && targetCandidates.containsAll(nonFixedValue.getValueCandidates()) }
                if (targetUnsolved.isEmpty()) {
                    return@segmentLoop
                }
                // create all possible combinations of n targetUnsolved cellValues, where n = combinationLength
                val unsolvedCellValueCombinations = allCombinations(targetUnsolved, combinationLength)
                unsolvedCellValueCombinations.forEach { nonFixedValueCombination ->
                    // for each combination found, remove value candidates in the combination from other segment cells in the segment
                    val combinationCandidates: Set<Int> = nonFixedValueCombination.map { it.getValueCandidates() }.flatten().toSet()
                    if (combinationCandidates.size == combinationLength) {
                        val cellValuesToRemoveCandidates
                                = (segment.cells.filter { cell -> !cell.isFixed && !cell.isSet }
                                .map { cell -> cell.cellValue as NonFixedValue }) - nonFixedValueCombination
                        if (cellValuesToRemoveCandidates.isNotEmpty()) {
                            val candidatesIntArray = combinationCandidates.toIntArray()
                            cellValuesToRemoveCandidates.forEach { nonFixedValue ->
                                if (nonFixedValue.removeValueCandidate(*candidatesIntArray)) {
                                    goNextCombinationSize = false
                                }
                            }
                        }
                    }
                }
            }
        }
//        synchronized(grid) {
//        }
//        //        handleEvents()
//assertCorrectGrid()
    }

    /**
     * * if 3 cells in a [GridSegment] have candidates, say, `[4, 7]`, `[1, 4, 7, 8]`, `[2, 4, 7, 8, 9]`,
     *   and none of the values `[4, 7, 8]` is a candidate value of the other cells in the same [GridSegment],
     *   the other values can be removed from these cells.
     *   So in these 3 cells, the candidate values are reduced like this: `[4, 7]` -> `[4, 7]`;  `[1, 4, 7, 8]` -> `[4, 7, 8]`; `[2, 4, 7, 8, 9]` -> `[4, 7, 8]`
     * * if 4 cells have candidates `[1, 2]`, `[1, 3, 4, 5, 9]`, `[1, 2, 5, 9]`, `[1, 2, 4, 5, 8]`, and no other cells in the same
     *   [GridSegment] have any of the values `[1, 2, 5, 9]`, the other values can be eliminated from these cells.
     *   So in these 4 cells, the remaining candidate values of those 4 cells will be `[1, 2]`, `[1, 5, 9]`, `[1, 2, 5, 9]`, `[1, 2, 5]`.
     */
    fun solvePolymorphicCombinations() {
//        synchronized(grid) {
        // gridSize divided by 2: In a grid of nine you may have excluding combinations of 1..8.
        // But if you have a combination of, say [1, 4, 5, 6, 7, 9], then there must also be a smaller combination,
        // in this case [1, 3, 8]. So it makes no sense to look for combinations of gridSize / 2 or larger, finding
        // larger combinations is much more costly in terms of computing power & memory
        var goNextCombinationSize = true
        (1..(grid.gridSize/2)).forEach combiLengthLoop@ { combinationLength ->
            if (!goNextCombinationSize) {
                // If we have found combinations of, say, 3, we do not proceed with combinations of 4,
                // we better handle events first (with less complicated solution methods)
                return
            }
            segments.forEach segmentLoop@{ segment ->
                // find all candidate values of unsolved cells of the segment that have at most `combinationLength` candidates
                val targetCandidates: SortedSet<Int> = cellValuesBySegment[segment]!!
                        .filter { cellValue -> !cellValue.isFixed && !cellValue.isSet }
                        .map { cellValue -> (cellValue as NonFixedValue).getValueCandidates() }
                        .filter { valueCandidates -> valueCandidates.size <= combinationLength }
                        .flatten()
                        .toSortedSet()
                if (targetCandidates.isEmpty()) {
                    return@segmentLoop
                }
                val candidateCombinationStream = allCombinations(targetCandidates, combinationLength)
                // Find all cellValues that have one or more candidates of the given combination
                candidateCombinationStream.forEach { candicateCombination ->
                    val cellValuesWithCandidateFromCombi = cellValuesBySegment[segment]!!
                            .filter { !it.isFixed && !it.isSet }
                            .map { it as NonFixedValue }
                            .filter { it.getValueCandidates().intersect(candicateCombination).isNotEmpty() }
                    if (cellValuesWithCandidateFromCombi.size == combinationLength) {
                        // if only n cellValues have candidates within a given combination of n values,
                        // all other candidate values can be removed from these cells
                        cellValuesWithCandidateFromCombi.forEach { nonFixedValue ->
                            val valuesToRemove = nonFixedValue.getValueCandidates() - candicateCombination
                            if (valuesToRemove.isNotEmpty()) {
                                if (nonFixedValue.removeValueCandidate(*valuesToRemove.toIntArray())) {
                                    goNextCombinationSize = false
                                }
                            }
                        }
                    }
                }
            }
        }
//        synchronized(grid) {
//        }
//        //        handleEvents()
//assertCorrectGrid()
//        }
//        //        handleEvents()
//assertCorrectGrid()
    }

    private fun solveGridPhase() {
        if (!isSolving || isSolved) {
            return
        }
        when (solvingPhase) {
            SOLVE_SINGLE_CANDIDATE_VALUES -> {
                solveSingularCandidates()
                handleEvents()
            }
            SOLVE_EXCLUDING_COMBINATIONS -> {
                solveIdenticalCandidateSets()
                solveSingleValueInSegments()
                handleEvents()
            }
            SOLVE_POLYMORPHIC_COMBINATIONS -> {
                solveExcludingSubsetCandidates()
                solvePolymorphicExclusions()
                solvePolymorphicCombinations()
                handleEvents()
            }
            MULTI_SEGMENT_COMBINATIONS -> {
                // TODO
            }
            CHAINING_TECHNIQUE -> {
                // TODO
            }
            else -> {
                if (isSolving) {
                    throw NotImplementedError("$solvingPhase is not implemented!")
                }
            }
        }
    }

    override val isSolved: Boolean
        get() = solvingPhase == GRID_SOLVED

    fun checkGridSolved(): Boolean {
        if (isSolved) {
            return true
        } else if (unSolvedNonFixedValues.isEmpty()) {
            solvingPhase = GRID_SOLVED
            return true
        }
        return false
    }

    override fun solveGrid() {
        log().info { "Grid to solve: $grid" }
        if (solvingPhase == PREPARED_FOR_SOLVING) {
            subscribeToEvents()
            solvingPhase = solvingPhase.nextSolvingPhase()!!
        }
        val cellCount = grid.gridSize * grid.gridSize
        val fixedCount = grid.cellList.filter { it.isFixed }.size
        var unSolvedCount: Int = unSolvedNonFixedValues.size
        var solvedCount = cellCount - fixedCount - unSolvedCount

        fun checkCellCounts() {
            if (cellCount != fixedCount + solvedCount + unSolvedCount) {
                // check it, just to be sure
                with (IllegalStateException("Error in cell counts! Total cells = $cellCount should be equal to: Fixed cells [$fixedCount] + Solved cells [$solvedCount] + Unsolved cells [$unSolvedCount]")) {
                    log().error { this }
                    throw this
                }
            }
        }

        try {
            do {
                log().info { "Solving grid ${grid.toStringCompact()}, phase = $solvingPhase" }
                solveGridPhase()
                unSolvedCount =  unSolvedNonFixedValues.size
                solvedCount = cellCount - fixedCount - unSolvedCount
                if (eventQueue.isEmpty()) {
                    solvingPhase = solvingPhase.nextSolvingPhase()
                            ?: throw GridNotSolvableException("Grid is not solved!")
                }
            } while (!checkGridSolved())
            log().info { "Grid solved! Solved grid = $grid" }
            assertCorrectGrid()

        } catch (gnse: GridNotSolvableException) {
            isSolvable = false
            log().info { "${gnse.message}. $this" +
                    "\nTotal cells = $cellCount; Fixed cells = $fixedCount; Solved cells = $solvedCount; Unsolved cells = $unSolvedCount" }
            if (log().isDebugEnabled) {
                val unSolved = ArrayList(unSolvedNonFixedValues)
                unSolved.sortBy { it.cell.rowIndex*grid.gridSize + it.cell.colIndex }
                val unsolvedOutput = FormattableList(unSolved.map { it.toString() })
                log().debug { "UnsolvedCells: \n$unsolvedOutput" }
            }
        } finally {
            checkCellCounts()
        }
    }

    protected fun handleEvent(valueEvent: ValueEvent) {
        if (isSolved) {
            return
        }
        log().trace { "Handling event: $valueEvent" }
//        synchronized(grid) {
            when (valueEvent) {
                is SetCellValueEvent -> {
                    handleSetCellValueEvent(valueEvent)
                }
                is CellRemoveCandidatesEvent -> {
                    handleRemoveCandidatesEvent(valueEvent)
                }
                else -> {
                    // not expected to be called, so warning:
                    // it would mean we subscribed to an event type that we are not willing to handle
                    log().warn { "Unhandled event type: ${valueEvent.javaClass.simpleName}; event=$valueEvent" }
                    return
                }
            }
//        }
        if (isSolving) {
            solvingPhase = firstSolvingPhase()
            solveGridPhase()
        }
    }

    protected fun handleEvents() {
        if (isSolved) {
            return
        }
        var event: ValueEvent
        log().trace { "Event count to handle: ${eventQueue.size}" }
        do { event = eventQueue.poll() ?: return
            handleEvent(event)
        } while (true)
    }

    override fun onEvent(valueEvent: ValueEvent) {
        if (isSolved) {
            return
        }
        log().trace { "$this received event: $valueEvent" }
        eventQueue.put(valueEvent)
    }

    override fun toString(): String {
        val gridToString: String
        if (gridToSolve == null) {
            gridToString = "null"
        } else {
            // If not solved yet, for not too big grids we display the grid with candidates, for easier debugging etc.
            gridToString = if (isSolved || grid.gridSize > 9) grid.toString() else gridWithCandidatesBoxFormatter.format(grid).toString()
        }
        return "${this.javaClass.simpleName}: isSolving=$isSolving, solvingPhase=$solvingPhase, grid=\n$gridToString"
    }

    /**
     * [SubSegment]s are artifacts that are used for solving only.
     * They have quite some similarities with [Row]s and [Col]s, and also with [Block]s, but constructed for solving purposes only,
     * not as an integral part of the Sudoku's [Grid] model.
     * [SubSegment]s may have references to [Row]s, [Col]s and [Block]s, but not the other way around: [Row]s, [Col]s and
     * [Block]s are not aware of [SubSegment]s.
     */
    protected sealed class SubSegment<out S: LinearSegment>(grid: Grid): GridElement(grid) {

        abstract val cells: Set<Cell>
        abstract val enclosingBlock: Block
        abstract val enclosingLinearSegment: S

        class HorizontalSubSegment(grid: Grid, val rowIndex: Int, val leftColIndex: Int): SubSegment<Row>(grid) {
            val rightColIndex: Int = leftColIndex + grid.blockSize - 1
            private val enclosingRow: Row = grid.rowList.filter { it.rowIndex == rowIndex }.first() // filter returns 1 row only
            override val enclosingLinearSegment: Row = enclosingRow
            override val enclosingBlock: Block = grid.blockList.filter { rowIndex in it.topRowIndex..it.bottomRowIndex && leftColIndex == it.leftColIndex }.first() // filter returns 1 row only
            override val cells: Set<Cell> = LinkedHashSet(enclosingRow.cells.filter { it.colIndex in leftColIndex..rightColIndex })

            override fun toString(): String = "${this.javaClass.simpleName}: [rowIndex=$rowIndex] [leftColIndex=$leftColIndex] [rightColIndex=$rightColIndex]" +
                    if (grid.blockSize <= 4) ("\n" + format(defaultGridFormatter)) else ""

            /** Concatenates the formatted [Cell]s */
            override fun format(formatter: SudokuFormatter): FormattableList {
                val formattedCells = cells.map { formatter.format(it) }.toTypedArray()
                return FormattableList(concatEach(*formattedCells))
            }
        }

        class VerticalSubSegment(grid: Grid, val colIndex: Int, val topRowIndex: Int): SubSegment<Col>(grid) {
            val botttomRowIndex: Int = topRowIndex + grid.blockSize - 1
            private val enclosingCol: Col = grid.colList.filter { it.colIndex == colIndex }.first() // filter returns 1 col only
            override val enclosingLinearSegment: Col = enclosingCol
            override val enclosingBlock: Block = grid.blockList.filter { colIndex in it.leftColIndex..it.rightColIndex && topRowIndex == it.topRowIndex }.first() // filter returns 1 row only
            override val cells: Set<Cell> = LinkedHashSet(enclosingCol.cells.filter { it.rowIndex in topRowIndex..botttomRowIndex })

            override fun toString(): String = "${this.javaClass.simpleName}: [colIndex=$colIndex] [topRowIndex=$topRowIndex] [botttomRowIndex=$botttomRowIndex]" +
                    if (grid.blockSize <= 4) ("\n" + format(defaultGridFormatter)) else ""

            /** Returns the formatted [Cell]s below each other */
            override fun format(formatter: SudokuFormatter): FormattableList {
                val formattedCells = cells.map { formatter.format(it).toString() }
                return FormattableList(formattedCells.fold(listOf(), {current, next -> current + next}))
            }
        }
    }

    /**
     * Enumerates phases in the [Grid] solving process. The order of the enum values is strictly relevant, with ascending complexity.
     * See also methods [nextPhase] and [nextSolvingPhase].
     * @param isSolving indication whether the [Grid] is actually solved in this phase
     */
    enum class GridSolvingPhase(val isSolving: Boolean) {
        /** Indicates that processing was not started yet ([Grid] not assigned yet). [isSolving] = `false` */
        NOT_STARTED(isSolving = false),
        /** Indicates that the [GridSolver] is ready to start processing ([Grid] has been assigned). [isSolving] = `false` */
        PREPARED_FOR_SOLVING(isSolving = false),

        /**
         * Phase [SOLVE_SINGLE_CANDIDATE_VALUES] indicates that the [GridSolver] is processing, searching for single candidate values within [GridSegment]s. [isSolving] = `true`.
         * * E.g. if a cell has `[5]` as it's only remaining candidate value, no other cells in the [GridSegment]s the cell is part of
         *   can have that value, so `[5]` can be eliminated from the other cells.
         * > Aka [Sudopedia: Naked Single](http://sudopedia.enjoysudoku.com/Naked_Single.html),
         *   [Decabit: Naked Single](http://www.decabit.com/Sudoku/NakedSingle), or Forced Digit.
         * > See: [solveSingularCandidates].
         */
        SOLVE_SINGLE_CANDIDATE_VALUES(isSolving = true),
        
        /**
         * Phase [SOLVE_EXCLUDING_COMBINATIONS] indicates that the [GridSolver] is processing,
         * searching for combinations of candidate values that exclude these values from other cells
         * in the [GridSegment]. [isSolving] = `true`.
         * E.g.,
         * * if 2 cells in a [GridSegment] both have `[2, 4]` as candidates, no other cells in that segment can have these
         *   candidate values, so these can be eliminated from the other [CellValue]s in the [GridSegment]
         * > See: [solveIdenticalCandidateSets].
         * > Aka [Sudopedia: Naked Pair](http://sudopedia.enjoysudoku.com/Naked_Pair.html),
         *   which is a [Naked Subset](http://www.decabit.com/Sudoku/NakedSubset) of size 2.
         * * if cells in a [GridSegment] have different combinations of values,
         *   e.g. `[1, 3, 6]`, `[2, 3, 7]`, `[1, 3, 7, 9]`, `[2, 4, 5, 8, 9]`
         *   and only 1 of those has `[6]` as a candidate value, that cell's value can be set to `[6]`.
         * > Aka [Sudopedia: Hidden Single](http://sudopedia.enjoysudoku.com/Hidden_Single.html)
         * > See: [solveSingleValueInSegments].
         */
        SOLVE_EXCLUDING_COMBINATIONS(isSolving = true),
        
        /**
         * Phase [SOLVE_POLYMORPHIC_COMBINATIONS] indicates that the [GridSolver] is processing, and searching for polymorphic combinations of candidate values. [isSolving] = `true`.
         * E.g.
         * * if in a [Block] a combination of values, say, `[2, 6]`, only exists in a single row part ([SubSegment]) of the [Block],
         *   (so these all have, say, [Block.rowIndex] = 4, thus the cells with [Block.rowIndex] 4 have candidates `[2, 4, 7]`, `[2, 6, 7]`
         *   and `[3, 6]`, and no other cells in that block have candidate `[2]` or `[6]`), than these values 2 and 6 can be removed from
         *   all other cells in the [Row] with [Row.rowIndex] = 4.
         *   > Aka Block Line or [Sudopedia: Locked Candidates type 1](http://sudopedia.enjoysudoku.com/Locked_Candidates.html#Type_1_.28Pointing.29)
         * * the same combination of values, say `[2, 6]` from the above example can also be removed from the cells in the [Block]
         *   that are not part of the [SubSegment] of the [Block].
         *   > Aka Block Line or [Sudopedia: Locked Candidates type 2](http://sudopedia.enjoysudoku.com/Locked_Candidates.html#Type_2_.28Claiming_or_Box-Line_Reduction.29)
         *
         * > See: [solveExcludingSubsetCandidates].
         *
         * * if 3 cells in a [GridSegment] have candidates, say, `[2, 8]`, `[3, 8]`, `[2, 8]`, then the values `[2, 3, 8]` can be removed
         *   from the candidates of all other cells in the same [GridSegment].
         * * if 4 cells have candidates `[1, 2]`, `[1, 2, 3]`, `[2, 3]`, `[1, 4]`, then the values `[1, 2, 3, 4]` can be removed
         *   from the candidates of all other cells in the same [GridSegment].
         * > Aka [Sudopedia: Naked Subset](http://sudopedia.enjoysudoku.com/Naked_Subset.html), [Decabit: Naked Subset](http://www.decabit.com/Sudoku/NakedSubset);
         *   depending on their size also Naked Pair, Naked Triple, Naked Quad etc.
         * > See: [solvePolymorphicExclusions].
         *
         * * if 3 cells in a [GridSegment] have candidates, say, `[4, 7]`, `[1, 4, 7, 8]`, `[2, 4, 7, 8, 9]`,
         *  and none of the values `[4, 7, 8]` is a candidate value of the other cells in the same [GridSegment], the other values can be
         *  removed from these cells.
         *  So in these 3 cells, the candidate values are reduced like this: `[4, 7]` -> `[4, 7]`;  `[1, 4, 7, 8]` -> `[4, 7, 8]`; `[2, 4, 7, 8, 9]` -> `[4, 7, 8]`
         * * if 4 cells have candidates `[1, 2]`, `[1, 3, 4, 5, 9]`, `[1, 2, 5, 9]`, `[1, 2, 4, 5, 8]`, and no other cells in the same
         *   [GridSegment] have any of the values `[1, 2, 5, 9]`, the other values can be eliminated from these cells.
         *   So in these 4 cells, the remaining candidate values of those 4 cells will be `[1, 2]`, `[1, 5, 9]`, `[1, 2, 5, 9]`, `[1, 2, 5]`.
         * > Aka [Sudopedia: Hidden Subset](http://sudopedia.enjoysudoku.com/Hidden_Subset.html), [Decabit: Hidden Subset](http://www.decabit.com/Sudoku/HiddenSubset).
         *   Depending on their size also Naked Pair, Naked Triple, Naked Quad etc.
         * > See: [solvePolymorphicCombinations].
         *
         * Re the 'polymorphic' in [SOLVE_POLYMORPHIC_COMBINATIONS], this is because none of the cells may have the
         * exact combination of values searched for, some candidate values of the combination searched for may not be
         * present in the touched cells, and cells may have candidate values that are not part of the combination searched for.
         * This is computationally relatively costly, so this method should be the last one before starting with [MULTI_SEGMENT_COMBINATIONS]
         * if unsolved candidates remain after all previous methods have been applied.
         */
        SOLVE_POLYMORPHIC_COMBINATIONS(isSolving = true),

        /**
         * The previous (less advanced) techniques can easily be deduced from the simple Sudoku rules.
         * But many many Sudoku puzzles appear to exist that can't be solved by these techniques (in fact, when trying to
         * solve the [hardest ever Sudoku "Golden Nugget"](http://www.sudokusnake.com/images/GoldenNugget.PNG),
         * _none_ of the cells are solved with these previous techniques! (see GridSolverIT)
         *
         * The following more advanced techniques are less obvious; undoubtedly they can be mathematically proven,
         * but here they are taken "as is".
         *
         * TBD!
         * * [X-Wing](http://www.decabit.com/Sudoku/XWing), [Swordfish](http://www.decabit.com/Sudoku/Swordfish),
         *   [Jellyfish](http://www.decabit.com/Sudoku/Jellyfish), [Squirmbag](http://sudopedia.enjoysudoku.com/Squirmbag.html)
         *   essentially, these are equal techniques but on different numbers of segments;
         * * [XY-Wing](http://www.decabit.com/Sudoku/XYWing) and [XYZ-Wing](http://www.decabit.com/Sudoku/XYZWing);
         *   these are equal techniques but with different numbers of candidate values;
         *   * Maybe also: [XY-Chain](http://www.decabit.com/Sudoku/XYChain), which is an extension of the XY-Wing technique
         * * Maybe: [Colouring-technique](http://www.decabit.com/Sudoku/Colouring)
         */
        MULTI_SEGMENT_COMBINATIONS(isSolving = true),

        /**
         * Even more advanced techniques, maybe including [Almost Locked Sets](http://sudopedia.enjoysudoku.com/Almost_Locked_Set.html)
         * > TBD !
         */
        CHAINING_TECHNIQUE(isSolving = true),

        /** Indicates that the [Grid] was solved successfully; [isSolving] = `false` */
        GRID_SOLVED(isSolving = false);

        /**
         * Given `this` [GridSolvingPhase], get the next one from [values];
         * if `this` is already the last one, then null
         */
        fun nextPhase(): GridSolvingPhase? {
            return if (ordinal < lastIndex) values()[ordinal+1] else null
        }

        /**
         * Given `this` [GridSolvingPhase], get the next one from [values] where [isSolving] `== true`,
         * if any; otherwise `null`
         */
        fun nextSolvingPhase(): GridSolvingPhase? {
            var phase = this.nextPhase()
            while (phase != null && !phase.isSolving) {
                phase = phase.nextPhase()
            }
            return if (phase != null && phase.isSolving) phase else null
        }

        companion object {
            private val lastIndex = values().lastIndex
            fun firstSolvingPhase(): GridSolvingPhase = values().first { it.isSolving }
            fun lastSolvingPhase(): GridSolvingPhase = values().last { it.isSolving }
        }
    }

}

class GridNotSetException(message: String) : IllegalStateException(message)

const val gridToSolvePropertyName = "gridToSolve"
