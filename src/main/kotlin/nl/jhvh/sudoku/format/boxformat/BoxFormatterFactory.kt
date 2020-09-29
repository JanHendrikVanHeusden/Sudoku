package nl.jhvh.sudoku.format.boxformat

import nl.jhvh.sudoku.format.SudokuFormatterFactory
import nl.jhvh.sudoku.format.boxformat.BoxFormatterFactory.FactoryInstance.factoryInstance
import nl.jhvh.sudoku.format.element.BlockFormatting
import nl.jhvh.sudoku.format.element.CellFormatting
import nl.jhvh.sudoku.format.element.CellValueFormatting
import nl.jhvh.sudoku.format.element.ColumnFormatting
import nl.jhvh.sudoku.format.element.GridFormatting
import nl.jhvh.sudoku.format.element.RowFormatting
import nl.jhvh.sudoku.format.simple.SimpleCellValueFormatter

private val cellValueFormatter: SimpleCellValueFormatter = SimpleCellValueFormatter()
private val cellBoxFormatter: CellBoxFormatter = CellBoxFormatter(cellValueFormatter)
private val rowBoxFormatter: RowBoxFormatter = RowBoxFormatter(cellBoxFormatter)
private val columnBoxFormatter: ColumnBoxFormatter = ColumnBoxFormatter(cellBoxFormatter)
private val blockBoxFormatter: BlockBoxFormatter = BlockBoxFormatter(cellBoxFormatter)
private val gridBoxFormatter = GridBoxWithRefsFormatter(GridBoxFormatter(rowBoxFormatter, columnBoxFormatter))

/**
 * This factory produces stateless formatters, so a single instance is sufficient, using [factoryInstance].
 *  * The formatters produced could have been completely static; however the factory pattern is chosen solely
 *    for testability (mocking and verification in unit tests).
 */
open class BoxFormatterFactory: SudokuFormatterFactory {

    override val cellValueFormatterInstance: CellValueFormatting = cellValueFormatter
    override val cellFormatterInstance: CellFormatting = cellBoxFormatter
    override val rowFormatterInstance: RowFormatting = rowBoxFormatter
    override val columnFormatterInstance: ColumnFormatting = columnBoxFormatter
    override val blockFormatterInstance: BlockFormatting = blockBoxFormatter
    override val gridFormatterInstance: GridFormatting = gridBoxFormatter

    companion object FactoryInstance {
        /** Singleton [BoxFormatterFactory] instance. */
        val factoryInstance: BoxFormatterFactory = BoxFormatterFactory()
    }
}
