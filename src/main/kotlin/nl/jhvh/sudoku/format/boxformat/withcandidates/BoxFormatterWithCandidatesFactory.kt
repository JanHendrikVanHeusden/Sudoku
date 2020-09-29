package nl.jhvh.sudoku.format.boxformat.withcandidates

import nl.jhvh.sudoku.format.SudokuFormatterFactory
import nl.jhvh.sudoku.format.boxformat.BlockBoxFormatter
import nl.jhvh.sudoku.format.boxformat.BoxFormatterFactory.FactoryInstance.factoryInstance
import nl.jhvh.sudoku.format.boxformat.CellBoxFormatter
import nl.jhvh.sudoku.format.boxformat.ColumnBoxFormatter
import nl.jhvh.sudoku.format.boxformat.GridBoxFormatter
import nl.jhvh.sudoku.format.boxformat.GridBoxWithRefsFormatter
import nl.jhvh.sudoku.format.boxformat.RowBoxFormatter
import nl.jhvh.sudoku.format.boxformat.withcandidates.BoxFormatterWithCandidatesFactory.BoxFactoryInstance.factoryInstance
import nl.jhvh.sudoku.format.element.BlockFormatting
import nl.jhvh.sudoku.format.element.CellFormatting
import nl.jhvh.sudoku.format.element.CellValueFormatting
import nl.jhvh.sudoku.format.element.ColumnFormatting
import nl.jhvh.sudoku.format.element.GridFormatting
import nl.jhvh.sudoku.format.element.RowFormatting

private val cellValueFormatter: CellValueWithCandidatesFormatter = CellValueWithCandidatesFormatter()
private val cellBoxFormatter: CellBoxFormatter = CellBoxFormatter(cellValueFormatter)
private val rowBoxFormatter: RowBoxFormatter = RowBoxFormatter(cellBoxFormatter)
private val columnBoxFormatter: ColumnBoxFormatter = ColumnBoxFormatter(cellBoxFormatter)
private val blockBoxFormatter: BlockBoxFormatter = BlockBoxFormatter(cellBoxFormatter)
private val gridBoxFormatter: GridFormatting = GridBoxWithRefsFormatter(GridBoxFormatter(rowBoxFormatter, columnBoxFormatter))

/**
 * This factory produces stateless formatters, so a single instance is sufficient, using [factoryInstance].
 *  * The formatters produced could have been completely static; however the factory pattern is chosen solely
 *    for testability (mocking and verification in unit tests).
 */
open class BoxFormatterWithCandidatesFactory: SudokuFormatterFactory {

    override val cellValueFormatterInstance: CellValueFormatting = cellValueFormatter
    override val cellFormatterInstance: CellFormatting = cellBoxFormatter
    override val rowFormatterInstance: RowFormatting = rowBoxFormatter
    override val columnFormatterInstance: ColumnFormatting = columnBoxFormatter
    override val blockFormatterInstance: BlockFormatting = blockBoxFormatter
    override val gridFormatterInstance: GridFormatting = gridBoxFormatter

    companion object BoxFactoryInstance {
        /** Singleton [BoxFormatterWithCandidatesFactory] instance. */
        val factoryInstance: BoxFormatterWithCandidatesFactory = BoxFormatterWithCandidatesFactory()
    }
}
