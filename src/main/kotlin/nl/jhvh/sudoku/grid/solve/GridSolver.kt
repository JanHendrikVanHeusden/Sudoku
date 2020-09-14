package nl.jhvh.sudoku.grid.solve

import nl.jhvh.sudoku.grid.event.ValueEvent
import nl.jhvh.sudoku.grid.event.ValueEventListener
import nl.jhvh.sudoku.grid.event.ValueEventType
import nl.jhvh.sudoku.grid.event.ValueEventType.SET_CELL_VALUE
import nl.jhvh.sudoku.grid.event.cellvalue.CellRemoveCandidatesEvent
import nl.jhvh.sudoku.grid.event.cellvalue.SetCellValueEvent
import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.grid.model.cell.CellValue
import nl.jhvh.sudoku.grid.model.cell.CellValue.FixedValue
import nl.jhvh.sudoku.grid.model.cell.CellValue.NonFixedValue
import nl.jhvh.sudoku.grid.model.segment.GridSegment
import nl.jhvh.sudoku.util.checkAndLog
import nl.jhvh.sudoku.util.log
import nl.jhvh.sudoku.util.requireAndLog
import java.util.Collections.unmodifiableMap
import java.util.Collections.unmodifiableSet
import java.util.concurrent.ConcurrentHashMap

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

    protected var subscribedToEvents = false
    protected var preparedForSolving = false

    protected val segments: Set<GridSegment> by lazy { with(grid) { (this.rowList + this.colList + this.blockList).toSet() } }

    protected val unSolvedValues: Set<NonFixedValue> by lazy {
        val map = grid.cellList
                .map { cell -> cell.cellValue }
                .filter { cellValue -> !cellValue.isFixed }
                .map { it as NonFixedValue }
        val concurrentHashSet = ConcurrentHashMap.newKeySet<NonFixedValue>(map.size)
        concurrentHashSet.addAll(map)
        concurrentHashSet
    }

    fun getUnSolvedCellValues(): Set<NonFixedValue> = unmodifiableSet(unSolvedValues)

    protected val segmentsByCellValue: Map<CellValue, Set<GridSegment>> by lazy {
        val segsByCellValue: MutableMap<CellValue, MutableSet<GridSegment>> = HashMap(grid.cellList.size)
        segments.forEach { segment ->
            segment.cells.forEach {cell ->
                // initialCapacity = 3: one for each row, column and block a cell is part of
                segsByCellValue.putIfAbsent(cell.cellValue, HashSet(3))
                segsByCellValue[cell.cellValue]!!.add(segment)
            }
        }
        unmodifiableMap(segsByCellValue)
    }

    protected fun subscribeToEvents(excludeFixed: Boolean = true, vararg valueEventTypes: ValueEventType = ValueEventType.values()) {
        valueEventTypes.forEach { eventType ->
            grid.cellList
                    .map { it.cellValue }
                    .filter { !excludeFixed || !it.isFixed }
                    .forEach { cellValue -> cellValue.subscribe(this, eventType) }
        }
    }

    override fun handleSetCellValueEvent(valueEvent: SetCellValueEvent) {
        val eventSource = valueEvent.eventSource
        val eventSourceSegments: Set<GridSegment> = segmentsByCellValue[eventSource]!!
        eventSourceSegments.forEach { gridSegment ->
                    gridSegment.cells.forEach { cell ->
                        if (cell.cellValue is NonFixedValue) {
                            if (cell.cellValue === eventSource) {
                                cell.cellValue.clearValueCandidates()
                            } else {
                                cell.cellValue.removeValueCandidate(valueEvent.newValue)
                            }
                        }
                        if (cell.cellValue === eventSource) {
                            cell.cellValue.unsubscribe(this, valueEvent.type)
                        }
                    }
                }
    }

    override fun handleRemoveCandidatesEvent(valueEvent: CellRemoveCandidatesEvent) {
        // TODO
    }

    protected fun prepareForSolving() {
        if (preparedForSolving) {
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
            preparedForSolving = true
        }
    }

    override fun solveGrid() {
        if (!subscribedToEvents) {
            subscribeToEvents()
            subscribedToEvents = true
        }
        val unsolvedByPrio = unSolvedValues
                .filter { !it.isSet }
                .groupBy { it.getValueCandidates().size }
        unsolvedByPrio.forEach {

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
            }
        }
        solveGrid()
    }

    override fun toString(): String {
        return "GridSolver(subscribedToEvents=$subscribedToEvents, preparedForSolving=$preparedForSolving)"
    }

}

class GridNotSetException(message: String) : IllegalStateException(message)

const val gridToSolvePropertyName = "gridToSolve"
