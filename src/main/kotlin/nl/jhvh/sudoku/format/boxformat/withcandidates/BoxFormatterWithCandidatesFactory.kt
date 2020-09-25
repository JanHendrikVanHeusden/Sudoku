package nl.jhvh.sudoku.format.boxformat.withcandidates

import nl.jhvh.sudoku.format.GridFormatterFactory
import nl.jhvh.sudoku.format.boxformat.BlockBoxFormatter
import nl.jhvh.sudoku.format.boxformat.BoxFormatterFactory.FactoryInstance.factoryInstance
import nl.jhvh.sudoku.format.boxformat.CellBoxFormatter
import nl.jhvh.sudoku.format.boxformat.ColumnBoxFormatter
import nl.jhvh.sudoku.format.boxformat.GridBoxFormatter
import nl.jhvh.sudoku.format.boxformat.RowBoxFormatter
import nl.jhvh.sudoku.format.boxformat.withcandidates.BoxFormatterWithCandidatesFactory.BoxFactoryInstance.factoryInstance

private val cellValueFormatter: CellValueWithCandidatesFormatter = CellValueWithCandidatesFormatter()
private val cellBoxFormatter: CellBoxFormatter = CellBoxFormatter(cellValueFormatter)
private val rowBoxFormatter: RowBoxFormatter = RowBoxFormatter(cellBoxFormatter)
private val columnBoxFormatter: ColumnBoxFormatter = ColumnBoxFormatter(cellBoxFormatter)
private val blockBoxFormatter: BlockBoxFormatter = BlockBoxFormatter(cellBoxFormatter)
private val gridBoxFormatter: GridBoxFormatter = GridBoxFormatter(rowBoxFormatter, columnBoxFormatter)

/**
 * This factory produces stateless formatters, so a single instance is sufficient, using [factoryInstance].
 *  * The formatters produced could have been completely static; however the factory pattern is chosen solely
 *    for testability (mocking and verification in unit tests).
 */
class BoxFormatterWithCandidatesFactory: GridFormatterFactory {

    override val cellValueFormatterInstance: CellValueWithCandidatesFormatter = cellValueFormatter
    override val cellBoxFormatterInstance: CellBoxFormatter = cellBoxFormatter
    override val rowBoxFormatterInstance: RowBoxFormatter = rowBoxFormatter
    override val columnBoxFormatterInstance: ColumnBoxFormatter = columnBoxFormatter
    override val blockBoxFormatterInstance: BlockBoxFormatter = blockBoxFormatter
    override val gridBoxFormatterInstance: GridBoxFormatter = gridBoxFormatter

    companion object BoxFactoryInstance {
        /** Singleton [BoxFormatterWithCandidatesFactory] instance. */
        val factoryInstance: BoxFormatterWithCandidatesFactory = BoxFormatterWithCandidatesFactory()
    }
}
