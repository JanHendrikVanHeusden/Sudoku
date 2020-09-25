package nl.jhvh.sudoku.grid.solve

import nl.jhvh.sudoku.grid.model.Grid

interface GridSolvable {

    val gridToSolve: Grid?

    val isSolving: Boolean

    val isSolved: Boolean

    val unSolvable: Boolean?

    fun solveGrid()

    override fun toString(): String

}