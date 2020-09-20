package nl.jhvh.sudoku.grid.solve

import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.format.SudokuFormatter
import nl.jhvh.sudoku.format.concatEach
import nl.jhvh.sudoku.grid.defaultGridToStringFormatter
import nl.jhvh.sudoku.grid.event.ValueEvent
import nl.jhvh.sudoku.grid.event.ValueEventListener
import nl.jhvh.sudoku.grid.event.ValueEventType
import nl.jhvh.sudoku.grid.event.ValueEventType.SET_CELL_VALUE
import nl.jhvh.sudoku.grid.event.cellvalue.CellRemoveCandidatesEvent
import nl.jhvh.sudoku.grid.event.cellvalue.SetCellValueEvent
import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.grid.model.GridElement
import nl.jhvh.sudoku.grid.model.cell.Cell
import nl.jhvh.sudoku.grid.model.cell.CellValue
import nl.jhvh.sudoku.grid.model.cell.CellValue.FixedValue
import nl.jhvh.sudoku.grid.model.cell.CellValue.NonFixedValue
import nl.jhvh.sudoku.grid.model.segment.Block
import nl.jhvh.sudoku.grid.model.segment.Col
import nl.jhvh.sudoku.grid.model.segment.GridSegment
import nl.jhvh.sudoku.grid.model.segment.Row
import nl.jhvh.sudoku.grid.solve.GridSolver.GridSolvingPhase.Companion.firstSolvingPhase
import nl.jhvh.sudoku.grid.solve.GridSolver.GridSolvingPhase.GRID_SOLVED
import nl.jhvh.sudoku.grid.solve.GridSolver.GridSolvingPhase.NOT_STARTED
import nl.jhvh.sudoku.grid.solve.GridSolver.GridSolvingPhase.PREPARED_FOR_SOLVING
import nl.jhvh.sudoku.grid.solve.GridSolver.GridSolvingPhase.SOLVE_EXCLUDING_COMBINATIONS
import nl.jhvh.sudoku.grid.solve.GridSolver.GridSolvingPhase.SOLVE_POLYMORPHIC_COMBINATIONS
import nl.jhvh.sudoku.grid.solve.GridSolver.GridSolvingPhase.SOLVE_SINGLE_CANDIDATE_VALUES
import nl.jhvh.sudoku.util.checkAndLog
import nl.jhvh.sudoku.util.log
import nl.jhvh.sudoku.util.requireAndLog
import java.util.Collections.unmodifiableMap
import java.util.Collections.unmodifiableSet
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

    val isSolving
        get() = solvingPhase.isSolving

    protected val segments: Set<GridSegment> by lazy { with(grid) { (this.rowList + this.colList + this.blockList).toSet() } }

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

    protected val unSolvedNonFixedValues: MutableSet<NonFixedValue> by lazy {
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

    private fun solveSingularCandidates(nonFixedValue: NonFixedValue) {
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
    }

    private fun solveSingularCandidates() {
        unSolvedNonFixedValues
                .filter { it.getValueCandidates().size <= 1}
                .forEach { nonFixedValue -> solveSingularCandidates(nonFixedValue) }
    }

    private fun solveSingleValueInSegments() {
        var segmentCellValuesWithCandidate: List<CellValue> = emptyList()
        segments.forEach {segment ->
            unSolvedNumbersBySegment[segment]!!.forEach { value ->
                val segmentCellValues = cellValuesBySegment[segment]!!
                segmentCellValuesWithCandidate = segmentCellValues
                        .filter {it is NonFixedValue && it.getValueCandidates().contains(value) }
                if (segmentCellValuesWithCandidate.size == 1) {
                    segmentCellValuesWithCandidate[0].setValue(value)
                }
            }
        }
    }

    fun solveExcludingSubsetValues() {
        // TODO
    }

    private fun solveIdenticalCandidateSets() {
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
    }

    private fun solvePolymorphicCombinations() {
        // TODO
    }

    private fun solveExcludingSubsetCombinations() {
        // TODO
    }

    private fun solveGridPhase() {
        if (!isSolving) {
            return
        }
        when (solvingPhase) {
            SOLVE_SINGLE_CANDIDATE_VALUES -> {
                solveSingularCandidates()
            }
            SOLVE_EXCLUDING_COMBINATIONS -> {
                solveIdenticalCandidateSets()
                solveSingleValueInSegments()
                solveExcludingSubsetValues()
            }
            SOLVE_POLYMORPHIC_COMBINATIONS -> {
                solvePolymorphicCombinations()
                solveExcludingSubsetCombinations()
            }
            else -> {
                if (isSolving) {
                    throw NotImplementedError("$solvingPhase is not implemented!")
                }
            }
        }
    }

    fun gridSolved(): Boolean {
        if (unSolvedNonFixedValues.isEmpty()) {
            solvingPhase = GRID_SOLVED
        }
        return solvingPhase == GRID_SOLVED
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
                solvingPhase = solvingPhase.nextSolvingPhase()?: throw GridNotSolvableException("Grid is not solved!")
            } while (!gridSolved())
            log().info { "Grid solved! Solved grid = $grid" }

        } catch (gnse: GridNotSolvableException) {
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

    override fun onEvent(valueEvent: ValueEvent) {
        log().trace { "$this received event: $valueEvent" }
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
        if (isSolving) {
            solvingPhase = firstSolvingPhase()
            solveGridPhase()
        }
    }

    override fun toString(): String {
        return "${this.javaClass.simpleName}: isSolving=$isSolving, solvingPhase=$solvingPhase, grid=$gridToSolve"
    }

    /**
     * [SubSegment]s are artifacts that are used for solving only.
     * They have quite some similarities with [Row]s and [Col]s, and also with [Block]s, but they are a superficial thing
     * constructed for solving purposes, not so much an integral part of the Sudoku's [Grid] model.
     * [SubSegment]s may have references to [Row]s, [Col]s and [Block]s, but not the other way around: [Row]s, [Col]s and
     * [Block]s are not aware of [SubSegment]s.
     */
    protected sealed class SubSegment(grid: Grid): GridElement(grid) {

        abstract val cells: Set<Cell>

        class HorizontalSubSegment(grid: Grid, val rowIndex: Int, val leftColIndex: Int): SubSegment(grid) {
            val rightColIndex: Int = leftColIndex + grid.blockSize - 1
            val enclosingRow: Row = grid.rowList.filter { it.rowIndex == rowIndex }.first() // filter returns 1 row only
            val enclosingBlock: Block = grid.blockList.filter { rowIndex in it.topRowIndex..it.bottomRowIndex && leftColIndex == it.leftColIndex }.first() // filter returns 1 row only
            override val cells: Set<Cell> = LinkedHashSet(enclosingRow.cells.filter { it.colIndex in leftColIndex..rightColIndex })

            override fun toString(): String = "${this.javaClass.simpleName}: [rowIndex=$rowIndex] [leftColIndex=$leftColIndex] [rightColIndex=$rightColIndex]" +
                    if (grid.blockSize <= 4) ("\n" + format(defaultGridToStringFormatter)) else ""

            /** Concatenates the formatted [Cell]s */
            override fun format(formatter: SudokuFormatter): FormattableList {
                val formattedCells = cells.map { formatter.format(it) }.toTypedArray()
                return FormattableList(concatEach(*formattedCells))
            }
        }

        class VerticalSubSegment(grid: Grid, val colIndex: Int, val topRowIndex: Int): SubSegment(grid) {
            val botttomRowIndex: Int = topRowIndex + grid.blockSize - 1
            val enclosingCol: Col = grid.colList.filter { it.colIndex == colIndex }.first() // filter returns 1 col only
            val enclosingBlock: Block = grid.blockList.filter { colIndex in it.leftColIndex..it.rightColIndex && topRowIndex == it.topRowIndex }.first() // filter returns 1 row only
            override val cells: Set<Cell> = LinkedHashSet(enclosingCol.cells.filter { it.rowIndex in topRowIndex..botttomRowIndex })

            override fun toString(): String = "${this.javaClass.simpleName}: [colIndex=$colIndex] [topRowIndex=$topRowIndex] [botttomRowIndex=$botttomRowIndex]" +
                    if (grid.blockSize <= 4) ("\n" + format(defaultGridToStringFormatter)) else ""

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
        NOT_STARTED(false),
        /** Indicates that the [GridSolver] is ready to start processing ([Grid] has been assigned). [isSolving] = `false` */
        PREPARED_FOR_SOLVING(false),

        /**
         * Phase [SOLVE_SINGLE_CANDIDATE_VALUES] indicates that the [GridSolver] is processing, searching for single candidate values within [GridSegment]s. [isSolving] = `true`.
         *  * E.g. if a cell has `[5]` as it's only remaining candidate value, no other cells in the [GridSegment]s the cell is part of
         *    can have that value, so `[5]` can be eliminated from the other cells.
         *     * See [solveSingularCandidates]
         */
        SOLVE_SINGLE_CANDIDATE_VALUES(true),
        
        /**
         * Phase [SOLVE_EXCLUDING_COMBINATIONS] indicates that the [GridSolver] is processing, searching for combinations of candidate values
         * that exclude these values from other cells in the [GridSegment]. [isSolving] = `true`.
         * E.g.,
         *  * if 2 cells in a [GridSegment] both have `[2, 4]` as candidates, no other cells in that segment can have these
         *    candidate values, so these can be eliminated from the other [CellValue]s in the [GridSegment]
         *     * See [solveIdenticalCandidateSets]
         *  * if cells in a [GridSegment] have different combinations of values, e.g. `[1, 3, 6]`, `[2, 3, 7]`, `[1, 3, 7, 9]`, `[2, 4, 5, 8, 9]`
         *    and only 1 of those has `[6]` as a candidate value, that cell's value can be set to `[6]`.
         *     * See [solveSingleValueInSegments]
         *  * if in a [Block] a value, say, `[2]`, only exists in a single row part ([SubSegment]) of the [Block], (so these all have, say,
         *    [Block.rowIndex] = 7, and the cells with [Block.rowIndex] 7 have candidates `[1, 4, 7]`, `[2, 7]` and `[2, 3]`,
         *    and no other cells in that block have candidate `[2]`), than that value 2 can be removed from all other cells
         *    in the [Row] with [Row.rowIndex] = 7.
         *     * See: [solveExcludingSubsetValues]
         */
        SOLVE_EXCLUDING_COMBINATIONS(true),
        
        /**
         * Phase [SOLVE_POLYMORPHIC_COMBINATIONS] indicates that the [GridSolver] is processing, and searching for polymorphic combinations of candidate values. [isSolving] = `true`.
         * E.g.
         *  * if 3 cells in a [GridSegment] have candidates, say, `[4, 7]`, `[1, 4, 7, 8]`, `[2, 4, 7, 8, 9]`, and none of the values
         *    in `[4, 7, 8]` is a candidate value of the other cells in the same [GridSegment], the other values can be removed
         *    from these cells.
         *    So in these 3 cells, the candidate values are reduced like this: `[4, 7]` -> `[4, 7]`;  `[1, 4, 7, 8]` -> `[4, 7, 8]`; `[2, 4, 7, 8, 9]` -> `[4, 7, 8]`
         *  * if 4 cells have candidates `[1, 2]`, `[1, 3, 4, 5, 9]`, `[1, 2, 5, 9]`, `[1, 2, 4, 5, 8]`, and no other cells in the same
         *    [GridSegment] have any of the values `[1, 2, 5, 9]`, the other values can be eliminated from these cells.
         *    So in these 4 cells, the remaining candidate values of those 4 cells will be `[1, 2]`, `[1, 5, 9]`, `[1, 2, 5, 9]`, `[1, 2, 5]`.
         *  * if in a [Block] a combination of values, say, `[2, 6]`, only exists in a single row part ([SubSegment]) of the [Block], (so these all have, say,
         *    [Block.rowIndex] = 4, thus the cells with [Block.rowIndex] 4 have candidates `[2, 4, 7]`, `[2, 6, 7]` and `[3, 6]`,
         *    and no other cells in that block have candidate `[2]` or `[6]`), than these values 2 and 6 can be removed from all other cells
         *    in the [Row] with [Row.rowIndex] = 4.
         *     * See: [solveExcludingSubsetCombinations]
         *
         * Re the 'polymorphic' in [SOLVE_POLYMORPHIC_COMBINATIONS], this is because none of the cells may have the exact combination of values searched for,
         * some candidate values of the combination searched for may not be present in the touched cells, and cells may have candidate values
         * that are not part of the combination searched for.
         * This is computationally relatively costly, so this method should be the last one if unsolved candidates remain after all other methods
         * have been applied.
         *  * See: [solvePolymorphicCombinations]
         */
        SOLVE_POLYMORPHIC_COMBINATIONS(true),

        /** Indicates that the [Grid] was solved successfully; [isSolving] = `false` */
        GRID_SOLVED(false);

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
