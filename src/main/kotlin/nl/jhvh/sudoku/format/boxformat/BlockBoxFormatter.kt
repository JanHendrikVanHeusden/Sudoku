package nl.jhvh.sudoku.format.boxformat

/* Copyright 2020 Jan-Hendrik van Heusden

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.format.element.BlockFormatting
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
class BlockBoxFormatter (private val cellBoxFormatter: CellBoxFormatter) : BlockFormatting, GridElementBoxFormatter<Block> {

    override fun format(block: Block): FormattableList {
        val nakedWithTopAndBottom = getTopBorder(block) + nakedFormat(block) + getBottomBorder(block)
        val leftBorder = listOf(getTopLeftEdge(block)) + getLeftBorder(block) + listOf(getBottomLeftEdge(block))
        val rightBorder = listOf(getTopRightEdge(block)) + getRightBorder(block) + listOf(getBottomRightEdge(block))
        return FormattableList(leftBorder concatEach nakedWithTopAndBottom concatEach rightBorder)
    }

    override fun nakedFormat(element: Block): FormattableList {
        val block = element
        // Block has n sub-rows (n = blockSize; sub-row as Cells of a Row insofar these Cells are part of the Block)
        // Create 2 lists-of-lists with identical structure, 1 with cells, 1 with formatted cells
        val cellsBySubRow = mutableListOf(mutableListOf<Cell>())
        val formattedCellsBySubRow = mutableListOf<FormattableList>()
        for (x in 0 until block.grid.blockSize) {
            cellsBySubRow.add(mutableListOf())
            cellsBySubRow[x].addAll(block.cells.filter { x == it.rowIndex - block.topRowIndex })

            val leftCellsWithRightBorder = concatEach(*cellsBySubRow[x].dropLast(1).map {
                cellBoxFormatter.nakedFormat(it) concatEach cellBoxFormatter.getRightBorder(it)
            }.toTypedArray())
            val rightCellNaked = cellBoxFormatter.nakedFormat(cellsBySubRow[x].last())
            formattedCellsBySubRow.add(FormattableList(leftCellsWithRightBorder concatEach rightCellNaked))

            if (x < block.grid.blockSize-1) {
                // add bottom border (except for last sub-row)
                val bottomBorder = concatEach(*cellsBySubRow[x].map {
                    cellBoxFormatter.getBottomBorder(it) concatEach
                            if (it.rightBorderIsBlockBorder()) listOf("") else listOf(cellBoxFormatter.getBottomRightEdge(it))
                }.toTypedArray())
                formattedCellsBySubRow.add(FormattableList(bottomBorder))
            }
        }
        return FormattableList(formattedCellsBySubRow.flatten())
    }

    override fun getLeftBorder(element: Block): FormattableList {
        val block = element
        val leftColCells = block.cells.filter { it.colIndex == block.leftColIndex }
        val result = mutableListOf<String>()
        leftColCells.dropLast(1).forEach {
            result.addAll(cellBoxFormatter.getLeftBorder(it) + listOf(cellBoxFormatter.getBottomLeftEdge(it)))
        }
        result.addAll(cellBoxFormatter.getLeftBorder(leftColCells.last()))
        return FormattableList(result)
    }

    override fun getRightBorder(element: Block): FormattableList {
        val block = element
        val rightColCells = block.cells.filter { it.colIndex == block.rightColIndex }
        val result = mutableListOf<String>()
        rightColCells.dropLast(1).forEach {
            result.addAll(cellBoxFormatter.getRightBorder(it) + listOf(cellBoxFormatter.getBottomRightEdge(it)))
        }
        result.addAll(cellBoxFormatter.getRightBorder(rightColCells.last()))
        return FormattableList(result)
    }

    override fun getTopBorder(element: Block): FormattableList {
        val block = element
        val topRowCells = block.cells.toList().subList(0, block.grid.blockSize)
        val topBorder = concatEach(*topRowCells.map {
            cellBoxFormatter.getTopBorder(it) concatEach
                    if (it.rightBorderIsBlockBorder()) listOf("") else listOf(cellBoxFormatter.getTopRightEdge(it))
        }.toTypedArray())
        return FormattableList(topBorder)
    }

    override fun getBottomBorder(element: Block): FormattableList {
        val block = element
        val bottomRowCells = block.cells.toList().subList(block.grid.gridSize - block.grid.blockSize, block.grid.gridSize)
        val bottomBorder = concatEach(*bottomRowCells.map {
            cellBoxFormatter.getBottomBorder(it) concatEach
                    if (it.rightBorderIsBlockBorder()) listOf("") else listOf(cellBoxFormatter.getBottomRightEdge(it))
        }.toTypedArray())
        return FormattableList(bottomBorder)
    }

    override fun getTopLeftEdge(element: Block): String {
        return cellBoxFormatter.getTopLeftEdge(element.cells.first())
    }

    override fun getTopRightEdge(element: Block): String {
        return cellBoxFormatter.getTopRightEdge(element.cells.toList()[element.grid.blockSize-1])
    }

    override fun getBottomLeftEdge(element: Block): String {
        return cellBoxFormatter.getBottomLeftEdge(element.cells.toList()[element.grid.gridSize - element.grid.blockSize])
    }

    override fun getBottomRightEdge(element: Block): String {
        return cellBoxFormatter.getBottomRightEdge(element.cells.last())
    }
}
