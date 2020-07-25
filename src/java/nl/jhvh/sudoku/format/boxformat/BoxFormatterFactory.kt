package nl.jhvh.sudoku.format.boxformat

import nl.jhvh.sudoku.format.simple.SimpleCellValueFormatter

class BoxFormatterFactory {

    val simpleCellValueFormatterInstance: SimpleCellValueFormatter = SimpleCellValueFormatter()
    val cellBoxFormatterInstance: CellBoxFormatter = CellBoxFormatter(simpleCellValueFormatterInstance)
    val rowBoxFormatterInstance: RowBoxFormatter = RowBoxFormatter(cellBoxFormatterInstance)
    val columnBoxFormatterInstance: ColumnBoxFormatter = ColumnBoxFormatter(cellBoxFormatterInstance)
    val blockBoxFormatterInstance: BlockBoxFormatter = BlockBoxFormatter(cellBoxFormatterInstance)
    val gridBoxFormatterInstance: GridBoxFormatter = GridBoxFormatter(rowBoxFormatterInstance, columnBoxFormatterInstance)

    companion object FactoryInstance {
        val instance: BoxFormatterFactory = BoxFormatterFactory()
    }
}
