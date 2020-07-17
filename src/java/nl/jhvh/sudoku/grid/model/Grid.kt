package nl.jhvh.sudoku.grid.model

import nl.jhvh.sudoku.base.incrementFromZero
import nl.jhvh.sudoku.format.Formattable
import nl.jhvh.sudoku.format.SudokuFormatter
import nl.jhvh.sudoku.grid.model.cell.Cell
import nl.jhvh.sudoku.grid.model.cell.CellRef
import nl.jhvh.sudoku.grid.model.segment.Block
import nl.jhvh.sudoku.grid.model.segment.Col
import nl.jhvh.sudoku.grid.model.segment.Row

/**
 * Class to represent a Sudoku grid.
 *  * Grids of different sizes can be represented, indicated by their [blockSize]
 *  * Square ones only ([Grid]s of `4*4` ([blockSize] = 2), `9*9` ([blockSize] = 3), `16*16` ([blockSize] = 4), etc.,
 *    but not `4*6`, `9*16` etc.)
 */
class Grid ( val blockSize: Int = 3) : Formattable {

    /** The length of each side = [blockSize] * [blockSize]  */
    var gridSize: Int = blockSize * blockSize
    /** Maximum value to be entered in a cell = [blockSize] * [blockSize]  */
    val maxValue: Int = blockSize * blockSize

    val cellList: List<Cell> = incrementFromZero(gridSize * gridSize)
            .map { Cell(this, colIndex = it % gridSize, rowIndex = it / gridSize)}

    val rowList: List<Row> = incrementFromZero(gridSize).map { Row(this, rowIndex = it) }
    val colList: List<Col> = incrementFromZero(gridSize).map { Col(this, colIndex = it) }
    val blockList: List<Block> = incrementFromZero(gridSize)
            .map { Block(this, leftXIndex = ((it*blockSize) % gridSize), topYIndex = (it/blockSize) * blockSize) }

    fun findCell(cellRef: String): Cell {
        return with(CellRef(cellRef)) { findCell(x, y) }
    }
    fun findCell(cellRef: CellRef): Cell {
        return findCell(cellRef.x, cellRef.y)
    }
    fun findCell(x: Int, y: Int): Cell {
        return cellList[x + y*gridSize]
    }

    fun fixCell(cell: Cell, value: Int) {
        cell.fixValue(value)
    }

    /** Technical [toString] method; for a functional representation, see [format]  */
    override fun toString(): String {
        return "${this.javaClass.simpleName}: (blockSize=$blockSize, gridSize=$gridSize)"
    }

    override fun format(formatter: SudokuFormatter): List<String> = formatter.format(this)
}
