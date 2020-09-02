package nl.jhvh.sudoku.grid.model

import nl.jhvh.sudoku.format.Formattable
import nl.jhvh.sudoku.grid.event.GridEventListener
import nl.jhvh.sudoku.grid.event.GridEventSource
import nl.jhvh.sudoku.grid.event.GridEventType
import java.util.concurrent.ConcurrentHashMap

/** Any part of a [Grid] */
abstract class GridElement(val grid: Grid): Formattable, GridEventSource {

    // lazy because otherwise the [Grid] initialization is not complete yet
    final override val maxValueLength: Int by lazy { this.grid.maxValueLength }

    final override val eventListeners: ConcurrentHashMap<GridEventType, MutableSet<GridEventListener>> = ConcurrentHashMap()

}
