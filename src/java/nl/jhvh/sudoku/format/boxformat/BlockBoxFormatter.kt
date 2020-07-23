package nl.jhvh.sudoku.format.boxformat

import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.format.element.BlockFormatter
import nl.jhvh.sudoku.grid.model.segment.Block

/**
 * Formatter to format a [Block] using box drawing characters and numbers
 *  * Box drawing characters: ║ │ ═ ─ ╔ ╗ ╚ ╝ ╤ ╧ ╦ ╩ ╟ ╢ ╠ ╣ ╬ ┼ ╪ ╫ etc.
 *
 * Typically for console output, to observe results of actions (grid construction, Sudoku solving,
 * testing etc.)
 */
@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE") // parameter `block: Block` (instead of `element: Block`)
class BlockBoxFormatter (private val cellFormatter: CellBoxFormatter) : BlockFormatter, ElementBoxFormattable<Block> {

    override fun format(block: Block): FormattableList {
        TODO("Not yet implemented")
    }

    override fun nakedFormat(block: Block): FormattableList {
        TODO("Not yet implemented")
    }

    override fun getLeftBorder(block: Block): FormattableList {
        TODO("Not yet implemented")
    }

    override fun getRightBorder(block: Block): FormattableList {
        TODO("Not yet implemented")
    }

    override fun getTopBorder(block: Block): FormattableList {
        TODO("Not yet implemented")
    }

    override fun getBottomBorder(block: Block): FormattableList {
        TODO("Not yet implemented")
    }

    override fun getTopLeftEdge(block: Block): String {
        TODO("Not yet implemented")
    }

    override fun getTopRightEdge(block: Block): String {
        TODO("Not yet implemented")
    }

    override fun getBottomLeftEdge(block: Block): String {
        TODO("Not yet implemented")
    }

    override fun getBottomRightEdge(block: Block): String {
        TODO("Not yet implemented")
    }
}
