package nl.jhvh.sudoku.format

import nl.jhvh.sudoku.grid.model.Grid;
import nl.jhvh.sudoku.grid.model.cell.Cell
import nl.jhvh.sudoku.grid.model.cell.CellValue
import nl.jhvh.sudoku.grid.model.segment.Block
import nl.jhvh.sudoku.grid.model.segment.Col
import nl.jhvh.sudoku.grid.model.segment.Row

/**
 * Interface to support formatting of Sudoku elements using the [Visitor Pattern](https://en.wikipedia.org/wiki/Visitor_pattern#Java_example).
 *  * The [format] methods return a formatted, human readable or machine readable (e.g. HTML) [String] representation of the element input.
 *  * This interface represents the visitor in the classic Visitor Pattern.
 *     * It lists the 'accepting' methods for the supported Sudoku element types.
 */
interface SudokuFormatter {

    /** @return A formatted, human readable or machine readable (e.g. HTML) [String] representation of a [CellValue] */
    fun format(cellValue: CellValue): String

    /** @return A formatted, human readable or machine readable (e.g. HTML) [String] representation of a [Cell] */
    fun format(cell: Cell): String

    /** @return A formatted, human readable or machine readable (e.g. HTML) [String] representation of a [Col] */
    fun format(col: Col): String

    /** @return A formatted, human readable [String] representation of a [Row] */
    fun format(row: Row): String

    /** @return A formatted, human readable or machine readable (e.g. HTML) [String] representation of a [Block] */
    fun format(block: Block): String

    /** @return A formatted, human readable or machine readable (e.g. HTML) [String] representation of a [Grid] */
    fun format(grid: Grid): String

}

/** Is the left border of the given [Cell] a [Grid] border?  */
val leftBorderIsGridBorder: (c: Cell) -> Boolean = { c -> colIndexIsLeftGridBorder(c.grid.gridSize, c.colIndex) }
/** Given the grid size, does the column index indicate a left border?
 *  No validation is performed, so illegal values (e.g index > gridSize, or negative values) may cause unexpected results. */
val colIndexIsLeftGridBorder: (gridSize: Int, colIndex: Int) -> Boolean = { _, colIndex -> colIndex == 0 }

/** Is the right border of the given [Cell] a [Grid] border?  */
val rightBorderIsGridBorder: (c: Cell) -> Boolean = { c -> colIndexIsRightGridBorder(c.grid.gridSize, c.colIndex) }
/** Given the grid size, does the column index indicate a right border?
 *  No validation is performed, so illegal values (e.g index > gridSize, or negative values) may cause unexpected results. */
val colIndexIsRightGridBorder: (gridSize: Int, colIndex: Int) -> Boolean = {gridSize, colIndex -> colIndex == gridSize - 1 }

/** Is the top border of the given [Cell] a [Grid] border?  */
val topBorderIsGridBorder: (c: Cell) -> Boolean = { c -> rowIndexIsTopGridBorder(c.grid.gridSize, c.rowIndex) }
/** Given the grid size, does the row index indicate a top border?
 *  No validation is performed, so illegal values (e.g index > gridSize, or negative values) may cause unexpected results. */
val rowIndexIsTopGridBorder: (gridSize: Int, rowIndex: Int) -> Boolean = { _, rowIndex -> rowIndex == 0 }

/** Is the bottom border of the given [Cell] a [Grid] border?  */
val bottomBorderIsGridBorder: (c: Cell) -> Boolean = { c -> rowIndexIsBottomGridBorder(c.grid.gridSize, c.rowIndex) }
/** Given the grid size, does the row index indicate a bottom border?
 *  No validation is performed, so illegal values (e.g index > gridSize, or negative values) may cause unexpected results. */
val rowIndexIsBottomGridBorder: (gridSize: Int, rowIndex: Int) -> Boolean = { gridSize, rowIndex -> rowIndex == gridSize - 1 }


/** Is the left border of the given [Cell] a [Block] border?  */
val leftBorderIsBlockBorder: (c: Cell) -> Boolean = { c -> colIndexIsLeftBlockBorder(c.grid.blockSize, c.colIndex) }
/** Given the block size, does the column index indicate a left block border?  */
val colIndexIsLeftBlockBorder: (blockSize: Int, colIndex: Int) -> Boolean = {blockSize, colIndex -> colIndex % blockSize == 0 }

/** Is the right border of the given [Cell] a [Block] border?  */
val rightBorderIsBlockBorder: (c: Cell) -> Boolean = { c -> colIndexIsRightBlockBorder(c.grid.blockSize, c.colIndex) }
/** Given the block size, does the column index indicate a right block border?
 *  No validation is performed, so illegal values (e.g index > gridSize, or negative values) may cause unexpected results. */
val colIndexIsRightBlockBorder: (blockSize: Int, colIndex: Int) -> Boolean = {blockSize, colIndex -> colIndex % blockSize == blockSize - 1 }

/** Is the top border of the given [Cell] a [Block] border?  */
val topBorderIsBlockBorder: (c: Cell) -> Boolean = { c -> rowIndexIsTopBlockBorder(c.grid.blockSize, c.rowIndex) }
/** Given the block size, does the row index indicate a top block border?
 *  No validation is performed, so illegal values (e.g index > gridSize, or negative values) may cause unexpected results. */
val rowIndexIsTopBlockBorder: (blockSize: Int, rowIndex: Int) -> Boolean = { blockSize, rowIndex -> rowIndex % blockSize == 0 }

/** Is the bottom border of the given [Cell] a [Block] border?  */
val bottomBorderIsBlockBorder: (c: Cell) -> Boolean = { c -> rowIndexIsBottomBlockBorder(c.grid.blockSize, c.rowIndex) }
/** Given the block size, does the row index indicate a bottom block border?
 *  No validation is performed, so illegal values (e.g index > gridSize, or negative values) may cause unexpected results. */
val rowIndexIsBottomBlockBorder: (blockSize: Int, rowIndex: Int) -> Boolean = { blockSize, rowIndex -> rowIndex % blockSize == blockSize - 1 }
