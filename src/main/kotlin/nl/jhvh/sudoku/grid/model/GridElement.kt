package nl.jhvh.sudoku.grid.model

import nl.jhvh.sudoku.format.Formattable

/** Any part of a [Grid] */
abstract class GridElement(val grid: Grid): GridStructural, Formattable {

    // lazy because otherwise the [Grid] initialization is not complete yet
    final override val maxValueLength: Int by lazy { this.grid.maxValueLength }

    abstract override fun toString(): String

}
