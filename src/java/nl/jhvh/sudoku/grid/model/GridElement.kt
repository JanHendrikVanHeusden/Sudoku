package nl.jhvh.sudoku.grid.model

import nl.jhvh.sudoku.format.Formattable

/** Any part of a [Grid] */
abstract class GridElement(val grid: Grid): Formattable {

    override val maxValueLength: Int by lazy { this.grid.maxValue.toString().length }
}
