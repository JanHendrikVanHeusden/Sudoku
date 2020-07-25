package nl.jhvh.sudoku.format.boxformat

import nl.jhvh.sudoku.format.boxformat.BoxFormatterFactory.FactoryInstance.factoryInstance
import nl.jhvh.sudoku.format.simple.SimpleCellValueFormatter

/**
 * This factory produces stateless formatters, so a single instance is sufficient, using [factoryInstance].
 *  * The formatters produced could have been completely static; however the factory pattern is chosen solely
 *    for testability (mocking and verification in unit tests).
 */
class BoxFormatterFactory {

    val simpleCellValueFormatterInstance: SimpleCellValueFormatter = SimpleCellValueFormatter()
    val cellBoxFormatterInstance: CellBoxFormatter = CellBoxFormatter(simpleCellValueFormatterInstance)
    val rowBoxFormatterInstance: RowBoxFormatter = RowBoxFormatter(cellBoxFormatterInstance)
    val columnBoxFormatterInstance: ColumnBoxFormatter = ColumnBoxFormatter(cellBoxFormatterInstance)
    val blockBoxFormatterInstance: BlockBoxFormatter = BlockBoxFormatter(cellBoxFormatterInstance)
    val gridBoxFormatterInstance: GridBoxFormatter = GridBoxFormatter(rowBoxFormatterInstance, columnBoxFormatterInstance)

    companion object FactoryInstance {
        /** Singleton [BoxFormatterFactory] instance. */
        val factoryInstance: BoxFormatterFactory = BoxFormatterFactory()
    }
}
