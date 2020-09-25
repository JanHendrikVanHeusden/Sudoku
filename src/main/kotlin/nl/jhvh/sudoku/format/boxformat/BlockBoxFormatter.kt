package nl.jhvh.sudoku.format.boxformat

import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.format.element.BlockFormatter
import nl.jhvh.sudoku.format.rightBorderIsBlockBorder
import nl.jhvh.sudoku.grid.model.cell.Cell
import nl.jhvh.sudoku.grid.model.segment.Block
import nl.jhvh.sudoku.util.concatEach

/**
 * Formatter to format a [Block] using box drawing characters and numbers. Stateless.
 *  * Box drawing characters: ║ │ ═ ─ ╔ ╗ ╚ ╝ ╤ ╧ ╦ ╩ ╟ ╢ ╠ ╣ ╬ ┼ ╪ ╫ etc.
 *
 * Typically for console output, to observe results of actions (block construction, Sudoku solving,
 * testing etc.)
 */
@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE") // parameter `block: Block` (instead of `element: Block`)
class BlockBoxFormatter (private val cellFormatter: CellBoxFormatter) : BlockFormatter, ElementBoxFormattable<Block> {

    override fun format(block: Block): FormattableList {
        val nakedWithTopAndBottom = getTopBorder(block) + nakedFormat(block) + getBottomBorder(block)
        val leftBorder = listOf(getTopLeftEdge(block)) + getLeftBorder(block) + listOf(getBottomLeftEdge(block))
        val rightBorder = listOf(getTopRightEdge(block)) + getRightBorder(block) + listOf(getBottomRightEdge(block))
        return FormattableList(leftBorder concatEach nakedWithTopAndBottom concatEach rightBorder)
    }

    override fun nakedFormat(block: Block): FormattableList {
        // Block has n sub-rows (n = blockSize; sub-row as Cells of a Row insofar these Cells are part of the Block)
        // Create 2 lists-of-lists with identical structure, 1 with cells, 1 with formatted cells
        val cellsBySubRow = mutableListOf(mutableListOf<Cell>())
        val formattedCellsBySubRow = mutableListOf<FormattableList>()
        for (x in 0 until block.grid.blockSize) {
            cellsBySubRow.add(mutableListOf())
            cellsBySubRow[x].addAll(block.cells.filter { x == it.rowIndex - block.topRowIndex })

            val leftCellsWithRightBorder = concatEach(*cellsBySubRow[x].dropLast(1).map {
                cellFormatter.nakedFormat(it) concatEach cellFormatter.getRightBorder(it)
            }.toTypedArray())
            val rightCellNaked = cellFormatter.nakedFormat(cellsBySubRow[x].last())
            formattedCellsBySubRow.add(FormattableList(leftCellsWithRightBorder concatEach rightCellNaked))

            if (x < block.grid.blockSize-1) {
                // add bottom border (except for last sub-row)
                val bottomBorder = concatEach(*cellsBySubRow[x].map {
                    cellFormatter.getBottomBorder(it) concatEach
                            if (it.rightBorderIsBlockBorder()) listOf("") else listOf(cellFormatter.getBottomRightEdge(it))
                }.toTypedArray())
                formattedCellsBySubRow.add(FormattableList(bottomBorder))
            }
        }
        return FormattableList(formattedCellsBySubRow.flatten())
    }

    override fun getLeftBorder(block: Block): FormattableList {
        val leftColCells = block.cells.filter { it.colIndex == block.leftColIndex }
        val result = mutableListOf<String>()
        leftColCells.dropLast(1).forEach {
            result.addAll(cellFormatter.getLeftBorder(it) + listOf(cellFormatter.getBottomLeftEdge(it)))
        }
        result.addAll(cellFormatter.getLeftBorder(leftColCells.last()))
        return FormattableList(result)
    }

    override fun getRightBorder(block: Block): FormattableList {
        val rightColCells = block.cells.filter { it.colIndex == block.rightColIndex }
        val result = mutableListOf<String>()
        rightColCells.dropLast(1).forEach {
            result.addAll(cellFormatter.getRightBorder(it) + listOf(cellFormatter.getBottomRightEdge(it)))
        }
        result.addAll(cellFormatter.getRightBorder(rightColCells.last()))
        return FormattableList(result)
    }

    override fun getTopBorder(block: Block): FormattableList {
        val topRowCells = block.cells.toList().subList(0, block.grid.blockSize)
        val topBorder = concatEach(*topRowCells.map {
            cellFormatter.getTopBorder(it) concatEach
                    if (it.rightBorderIsBlockBorder()) listOf("") else listOf(cellFormatter.getTopRightEdge(it))
        }.toTypedArray())
        return FormattableList(topBorder)
    }

    override fun getBottomBorder(block: Block): FormattableList {
        val bottomRowCells = block.cells.toList().subList(block.grid.gridSize - block.grid.blockSize, block.grid.gridSize)
        val bottomBorder = concatEach(*bottomRowCells.map {
            cellFormatter.getBottomBorder(it) concatEach
                    if (it.rightBorderIsBlockBorder()) listOf("") else listOf(cellFormatter.getBottomRightEdge(it))
        }.toTypedArray())
        return FormattableList(bottomBorder)
    }

    override fun getTopLeftEdge(block: Block): String {
        return cellFormatter.getTopLeftEdge(block.cells.first())
    }

    override fun getTopRightEdge(block: Block): String {
        return cellFormatter.getTopRightEdge(block.cells.toList()[block.grid.blockSize-1])
    }

    override fun getBottomLeftEdge(block: Block): String {
        return cellFormatter.getBottomLeftEdge(block.cells.toList()[block.grid.gridSize - block.grid.blockSize])
    }

    override fun getBottomRightEdge(block: Block): String {
        return cellFormatter.getBottomRightEdge(block.cells.last())
    }
}
