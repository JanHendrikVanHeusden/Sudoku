package nl.jhvh.sudoku.grid

import io.mockk.every
import io.mockk.excludeRecords
import io.mockk.mockk
import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.grid.model.cell.Cell
import nl.jhvh.sudoku.grid.model.cell.CellValue
import nl.jhvh.sudoku.grid.model.segment.Block
import nl.jhvh.sudoku.grid.model.segment.Col
import nl.jhvh.sudoku.grid.model.segment.Row
import org.junit.jupiter.api.BeforeEach

/**
 * Base class for tests that require a [Grid] mock fully populated with [Cell] and [CellValue] mocks,
 * and with segments: [Row], [Col] and [Block] mocks.
 * @see GridWithCellsTestBase
 */
internal abstract class GridWithCellsAndSegmentsTestBase(blockSize: Int): GridWithCellsTestBase(blockSize) {

    protected lateinit var gridMockRowList: List<Row>
    protected lateinit var gridMockColList: List<Col>
    protected lateinit var gridMockBlockList: List<Block>

    @BeforeEach
    fun setUpSegments() {
        setUpGridMockRowList()
        setUpGridMockColList()
        setUpGridMockBlockList()
    }

    protected fun setUpGridMockRowList() {
        val rowList: MutableList<Row> = mutableListOf()
        for (rowIndex in 0 until gridMock.gridSize) {
            val rowCells: MutableSet<Cell> = mutableSetOf()
            val row: Row = mockk(relaxed = true)
            every { row.rowIndex } returns rowIndex
            every { row.toString() } returns "Row mock; rowIndex=${row.rowIndex}"
            rowCells.addAll(gridMock.cellList.filter { it.rowIndex == rowIndex })
            every {row.cells} returns rowCells
            excludeRecords { row.rowIndex }
            excludeRecords { row.cells }
            rowList.add(row)
        }
        gridMockRowList = rowList
        every { gridMock.rowList } returns gridMockRowList
    }

    protected fun setUpGridMockColList() {
        val colList: MutableList<Col> = mutableListOf()
        for (colIndex in 0 until gridMock.gridSize) {
            val colCells: MutableSet<Cell> = mutableSetOf()
            val col: Col = mockk(relaxed = true)
            every { col.colIndex } returns colIndex
            every { col.toString() } returns "Col mock; rowIndex=${col.colIndex}"
            colCells.addAll(gridMock.cellList.filter { it.colIndex == colIndex })
            every {col.cells} returns colCells
            excludeRecords { col.colIndex }
            excludeRecords { col.cells }
            colList.add(col)
        }
        gridMockColList = colList
        every { gridMock.colList } returns gridMockColList
    }

    protected fun setUpGridMockBlockList(){
        val blockList: MutableList<Block> = mutableListOf()
        for (blockNo in 0 until gridMock.gridSize) {
            val blockCells: MutableSet<Cell> = mutableSetOf()
            val block: Block = mockk(relaxed = true)
            every { block.leftColIndex } returns (blockNo * gridMock.blockSize) % gridMock.gridSize
            every { block.topRowIndex } returns (blockNo / gridMock.blockSize) * gridMock.blockSize
            every { block.rightColIndex } returns block.leftColIndex + gridMock.blockSize - 1
            every { block.topRowIndex } returns (blockNo / gridMock.blockSize) * gridMock.blockSize
            every { block.bottomRowIndex } returns block.topRowIndex + gridMock.blockSize - 1
            every { block.toString() } returns
                    "Block mock; leftColIndex=${block.leftColIndex}, rightColIndex=${block.rightColIndex}, topRowIndex=${block.topRowIndex}, bottomRowIndex=${block.bottomRowIndex}"
            val cells = gridMock.cellList.filter {
                it.rowIndex in block.topRowIndex..block.bottomRowIndex
                        && it.colIndex in block.leftColIndex..block.rightColIndex
            }
            blockCells.addAll(cells)
            every {block.cells} returns blockCells
            excludeRecords { block.topRowIndex }
            excludeRecords { block.bottomRowIndex }
            excludeRecords { block.leftColIndex }
            excludeRecords { block.rightColIndex }
            excludeRecords { block.cells }
            blockList.add(block)
        }
        gridMockBlockList = blockList
        every { gridMock.blockList } returns gridMockBlockList
    }

    /** Used by [clearAllGridMocks] */
    override val allGridMocks: Array<Any> by lazy {
        val mockkList = mutableListOf<Any>()
        mockkList.addAll(super.allGridMocks)
        mockkList.addAll(gridMock.rowList)
        mockkList.addAll(gridMock.colList)
        mockkList.addAll(gridMock.blockList)
        mockkList.toTypedArray()
    }

}
