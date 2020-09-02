package nl.jhvh.sudoku.format

import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.grid.model.cell.Cell
import nl.jhvh.sudoku.grid.model.segment.Block

val lineSeparator: String = System.lineSeparator()

/** Is the left border of the given [Cell] a [Grid] border?  */
fun Cell.leftBorderIsGridBorder(): Boolean =  colIndexIsLeftGridBorder(this.grid.gridSize, this.colIndex) 
/** Given the grid size, does the column index indicate a left border?
 *  No validation is performed, so illegal values (e.g index > gridSize, or negative values) may cause unexpected results. */
val colIndexIsLeftGridBorder: (gridSize: Int, colIndex: Int) -> Boolean = { _, colIndex -> colIndex == 0 }

/** Is the right border of the given [Cell] a [Grid] border?  */
fun Cell.rightBorderIsGridBorder(): Boolean =  colIndexIsRightGridBorder(this.grid.gridSize, this.colIndex) 
/** Given the grid size, does the column index indicate a right border?
 *  No validation is performed, so illegal values (e.g index > gridSize, or negative values) may cause unexpected results. */
val colIndexIsRightGridBorder: (gridSize: Int, colIndex: Int) -> Boolean = {gridSize, colIndex -> colIndex == gridSize - 1 }

/** Is the top border of the given [Cell] a [Grid] border?  */
fun Cell.topBorderIsGridBorder(): Boolean =  rowIndexIsTopGridBorder(this.grid.gridSize, this.rowIndex) 
/** Given the grid size, does the row index indicate a top border?
 *  No validation is performed, so illegal values (e.g index > gridSize, or negative values) may cause unexpected results. */
val rowIndexIsTopGridBorder: (gridSize: Int, rowIndex: Int) -> Boolean = { _, rowIndex -> rowIndex == 0 }

/** Is the bottom border of the given [Cell] a [Grid] border?  */
fun Cell.bottomBorderIsGridBorder(): Boolean =  rowIndexIsBottomGridBorder(this.grid.gridSize, this.rowIndex) 
/** Given the grid size, does the row index indicate a bottom border?
 *  No validation is performed, so illegal values (e.g index > gridSize, or negative values) may cause unexpected results. */
val rowIndexIsBottomGridBorder: (gridSize: Int, rowIndex: Int) -> Boolean = { gridSize, rowIndex -> rowIndex == gridSize - 1 }


/** Is the left border of the given [Cell] a [Block] border?  */
fun Cell.leftBorderIsBlockBorder(): Boolean =  colIndexIsLeftBlockBorder(this.grid.blockSize, this.colIndex) 
/** Given the block size, does the column index indicate a left block border?  */
val colIndexIsLeftBlockBorder: (blockSize: Int, colIndex: Int) -> Boolean = {blockSize, colIndex -> colIndex % blockSize == 0 }

/** Is the right border of the given [Cell] a [Block] border?  */
fun Cell.rightBorderIsBlockBorder(): Boolean =  colIndexIsRightBlockBorder(this.grid.blockSize, this.colIndex) 
/** Given the block size, does the column index indicate a right block border?
 *  No validation is performed, so illegal values (e.g index > gridSize, or negative values) may cause unexpected results. */
val colIndexIsRightBlockBorder: (blockSize: Int, colIndex: Int) -> Boolean = {blockSize, colIndex -> colIndex % blockSize == blockSize - 1 }

/** Is the top border of the given [Cell] a [Block] border?  */
fun Cell.topBorderIsBlockBorder(): Boolean =  rowIndexIsTopBlockBorder(this.grid.blockSize, this.rowIndex) 
/** Given the block size, does the row index indicate a top block border?
 *  No validation is performed, so illegal values (e.g index > gridSize, or negative values) may cause unexpected results. */
val rowIndexIsTopBlockBorder: (blockSize: Int, rowIndex: Int) -> Boolean = { blockSize, rowIndex -> rowIndex % blockSize == 0 }

/** Is the bottom border of the given [Cell] a [Block] border?  */
fun Cell.bottomBorderIsBlockBorder(): Boolean =  rowIndexIsBottomBlockBorder(this.grid.blockSize, this.rowIndex) 
/** Given the block size, does the row index indicate a bottom block border?
 *  No validation is performed, so illegal values (e.g index > gridSize, or negative values) may cause unexpected results. */
val rowIndexIsBottomBlockBorder: (blockSize: Int, rowIndex: Int) -> Boolean = { blockSize, rowIndex -> rowIndex % blockSize == blockSize - 1 }
