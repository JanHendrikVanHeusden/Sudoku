package nl.jhvh.sudoku.format.boxformat

import nl.jhvh.sudoku.format.Formattable.FormattableList
import nl.jhvh.sudoku.format.element.BlockFormatter
import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.grid.model.GridBuilder
import nl.jhvh.sudoku.grid.model.segment.Block
import org.junit.jupiter.api.BeforeEach

/**
 * Formatter to format a [Block] using box drawing characters and numbers
 *  * Box drawing characters: ║ │ ═ ─ ╔ ╗ ╚ ╝ ╤ ╧ ╦ ╩ ╟ ╢ ╠ ╣ ╬ ┼ ╪ ╫ etc.
 *
 * Typically for console output, to observe results of actions (grid construction, Sudoku solving,
 * testing etc.)
 */
@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE") // parameter `block: Block` (instead of `element: Block`)
class BlockBoxFormatter: BlockFormatter, BoxBorderingFormatter<Block> {

    private val subject = BlockBoxFormatter()

    /**
     * * `A, B, C,` etc. denote rows (top row is `A` => y = 0)
     * * `1, 2, 3,` etc. denote columns (left column is `1`, => x = 0)
     * * So
     *     * `A1` corresponds with (x, y) = (0, 0),
     *     * `A2` corresponds with (x, y) = (1, 0),
     *     * ...
     *     * `B5` corresponds with (x, y) = (4, 1)
     *     * ... etc.
     *
     * Block size default = 3
     */
    private lateinit var grid9: Grid

    @BeforeEach
    fun setUp() {
        grid9 = GridBuilder()
                .fix("A1", 7) // x = 0, y = 0
                .fix("B1", 4) // x = 0, y = 1
                .fix("B3", 2) // x = 2, y = 1
                .fix("C7", 8) // x = 6, y = 2
                .fix("I6", 3) // x = 5, y = 8
                .build()

        grid9.findCell(5, 3).cellValue.setValue(4)
        grid9.findCell(7, 3).cellValue.setValue(2)
    }

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
