package nl.jhvh.sudoku.grid.solve

import nl.jhvh.sudoku.grid.model.Grid

interface GridSolvable {

    val gridToSolve: Grid?

    fun solveGrid()

    override fun toString(): String

}