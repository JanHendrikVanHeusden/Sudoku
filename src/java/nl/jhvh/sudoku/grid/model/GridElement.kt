package nl.jhvh.sudoku.grid.model

import nl.jhvh.sudoku.format.Formattable

/** Any part of a [Grid] */
abstract class GridElement(val grid: Grid): Formattable {

    // lazy because otherwise the [Grid] initialization is not complete yet
    override val maxValueLength: Int by lazy { this.grid.maxValue.toString().length }
}
