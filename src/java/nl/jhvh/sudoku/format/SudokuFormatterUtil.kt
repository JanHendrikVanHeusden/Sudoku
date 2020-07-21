package nl.jhvh.sudoku.format

import nl.jhvh.sudoku.format.boxformat.BAR_DOUBLE
import nl.jhvh.sudoku.format.boxformat.BAR_SINGLE
import nl.jhvh.sudoku.format.boxformat.CROSS_DOUBLE
import nl.jhvh.sudoku.format.boxformat.CROSS_FLAT_DOUBLE_BAR_SINGLE
import nl.jhvh.sudoku.format.boxformat.CROSS_FLAT_SINGLE_BAR_DOUBLE
import nl.jhvh.sudoku.format.boxformat.CROSS_SINGLE
import nl.jhvh.sudoku.format.boxformat.EDGE_BOTTOM_LEFT_DOUBLE
import nl.jhvh.sudoku.format.boxformat.EDGE_BOTTOM_RIGHT_DOUBLE
import nl.jhvh.sudoku.format.boxformat.EDGE_TOP_LEFT_DOUBLE
import nl.jhvh.sudoku.format.boxformat.EDGE_TOP_RIGHT_DOUBLE
import nl.jhvh.sudoku.format.boxformat.FLAT_DOUBLE
import nl.jhvh.sudoku.format.boxformat.FLAT_SINGLE
import nl.jhvh.sudoku.format.boxformat.JUNCTION_BAR_DOUBLE_LEFTWARD_DOUBLE
import nl.jhvh.sudoku.format.boxformat.JUNCTION_BAR_DOUBLE_LEFTWARD_SINGLE
import nl.jhvh.sudoku.format.boxformat.JUNCTION_BAR_DOUBLE_RIGHTWARD_DOUBLE
import nl.jhvh.sudoku.format.boxformat.JUNCTION_BAR_DOUBLE_RIGHTWARD_SINGLE
import nl.jhvh.sudoku.format.boxformat.JUNCTION_BOTTOM_DOUBLE_UP_DOUBLE
import nl.jhvh.sudoku.format.boxformat.JUNCTION_BOTTOM_DOUBLE_UP_SINGLE
import nl.jhvh.sudoku.format.boxformat.JUNCTION_TOP_DOUBLE_DOWN_DOUBLE
import nl.jhvh.sudoku.format.boxformat.JUNCTION_TOP_DOUBLE_DOWN_SINGLE
import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.grid.model.cell.Cell
import nl.jhvh.sudoku.grid.model.segment.Block

val lineEnd: String = System.lineSeparator()

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


fun Cell.leftBorder(): Char {
    return when {
        (this.leftBorderIsGridBorder() || this.leftBorderIsBlockBorder()) -> BAR_DOUBLE
        else -> BAR_SINGLE
    }
}

fun Cell.rightBorder(): Char {
    return when {
        (this.rightBorderIsGridBorder() || this.rightBorderIsBlockBorder()) -> BAR_DOUBLE
        else -> BAR_SINGLE
    }
}

fun Cell.topBorder(): Char {
    return when {
        (this.topBorderIsGridBorder() || this.topBorderIsBlockBorder()) -> FLAT_DOUBLE
        else -> FLAT_SINGLE
    }
}

fun Cell.bottomBorder(): Char {
    return when {
        (this.bottomBorderIsGridBorder() || this.bottomBorderIsBlockBorder()) -> FLAT_DOUBLE
        else -> FLAT_SINGLE
    }
}

fun Cell.topLeftEdge(): Char {
    return when {
        (this.topBorderIsGridBorder() && this.leftBorderIsGridBorder()) -> EDGE_TOP_LEFT_DOUBLE
        (this.topBorderIsGridBorder() && this.leftBorderIsBlockBorder()) -> JUNCTION_TOP_DOUBLE_DOWN_DOUBLE
        (this.topBorderIsGridBorder()) -> JUNCTION_TOP_DOUBLE_DOWN_SINGLE

        (this.leftBorderIsGridBorder() && this.topBorderIsBlockBorder()) -> JUNCTION_BAR_DOUBLE_RIGHTWARD_DOUBLE
        (this.leftBorderIsGridBorder() && !this.topBorderIsBlockBorder()) -> JUNCTION_BAR_DOUBLE_RIGHTWARD_SINGLE

        (this.topBorderIsBlockBorder() && this.leftBorderIsBlockBorder()) -> CROSS_DOUBLE
        (this.topBorderIsBlockBorder()) -> CROSS_FLAT_DOUBLE_BAR_SINGLE
        (this.leftBorderIsBlockBorder()) -> CROSS_FLAT_SINGLE_BAR_DOUBLE
        else -> CROSS_SINGLE
    }
}

fun Cell.topRightEdge(): Char {
    return when {
        (this.topBorderIsGridBorder() && this.rightBorderIsGridBorder()) -> EDGE_TOP_RIGHT_DOUBLE
        (this.topBorderIsGridBorder() && this.rightBorderIsBlockBorder()) -> JUNCTION_TOP_DOUBLE_DOWN_DOUBLE
        (this.topBorderIsGridBorder()) -> JUNCTION_TOP_DOUBLE_DOWN_SINGLE

        (this.rightBorderIsGridBorder() && this.topBorderIsBlockBorder()) -> JUNCTION_BAR_DOUBLE_LEFTWARD_DOUBLE
        (this.rightBorderIsGridBorder()) -> JUNCTION_BAR_DOUBLE_LEFTWARD_SINGLE

        (this.topBorderIsBlockBorder() && this.rightBorderIsBlockBorder()) -> CROSS_DOUBLE
        (this.topBorderIsBlockBorder()) -> CROSS_FLAT_DOUBLE_BAR_SINGLE
        (this.rightBorderIsBlockBorder()) -> CROSS_FLAT_SINGLE_BAR_DOUBLE
        else -> CROSS_SINGLE
    }
}

fun Cell.bottomLeftEdge(): Char {
    return when {
        (this.bottomBorderIsGridBorder() && this.leftBorderIsGridBorder()) -> EDGE_BOTTOM_LEFT_DOUBLE
        (this.bottomBorderIsGridBorder() && this.leftBorderIsBlockBorder()) -> JUNCTION_BOTTOM_DOUBLE_UP_DOUBLE
        (this.bottomBorderIsGridBorder()) -> JUNCTION_BOTTOM_DOUBLE_UP_SINGLE

        (this.leftBorderIsGridBorder() && this.bottomBorderIsBlockBorder()) -> JUNCTION_BAR_DOUBLE_RIGHTWARD_DOUBLE
        (this.leftBorderIsGridBorder()) -> JUNCTION_BAR_DOUBLE_RIGHTWARD_SINGLE

        (this.bottomBorderIsBlockBorder() && this.leftBorderIsBlockBorder()) -> CROSS_DOUBLE
        (this.bottomBorderIsBlockBorder()) -> CROSS_FLAT_DOUBLE_BAR_SINGLE
        (this.leftBorderIsBlockBorder()) -> CROSS_FLAT_SINGLE_BAR_DOUBLE
        else -> CROSS_SINGLE
    }
}

fun Cell.bottomRightEdge(): Char {
    return when {
        (this.bottomBorderIsGridBorder() && this.rightBorderIsGridBorder()) -> EDGE_BOTTOM_RIGHT_DOUBLE
        (this.bottomBorderIsGridBorder() && this.rightBorderIsBlockBorder()) -> JUNCTION_BOTTOM_DOUBLE_UP_DOUBLE
        (this.bottomBorderIsGridBorder()) -> JUNCTION_BOTTOM_DOUBLE_UP_SINGLE

        (this.rightBorderIsGridBorder() && this.bottomBorderIsBlockBorder()) -> JUNCTION_BAR_DOUBLE_LEFTWARD_DOUBLE
        (this.rightBorderIsGridBorder()) -> JUNCTION_BAR_DOUBLE_LEFTWARD_SINGLE

        (this.bottomBorderIsBlockBorder() && this.rightBorderIsBlockBorder()) -> CROSS_DOUBLE
        (this.bottomBorderIsBlockBorder()) -> CROSS_FLAT_DOUBLE_BAR_SINGLE
        (this.rightBorderIsBlockBorder()) -> CROSS_FLAT_SINGLE_BAR_DOUBLE
        else -> CROSS_SINGLE
    }
}
