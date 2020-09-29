package nl.jhvh.sudoku.format.boxformat

import nl.jhvh.sudoku.format.SudokuFormatterFactory
import nl.jhvh.sudoku.format.boxformat.withcandidates.BoxFormatterWithCandidatesFactory
import nl.jhvh.sudoku.format.element.GridFormatting
import nl.jhvh.sudoku.grid.model.Grid

internal class GridWithRefsBoxFormatterTest {

    fun test() {
        // TODO: Assertions !
        val gridBoxFormatter = BoxFormatterWithCandidatesFactory.factoryInstance.gridFormatterInstance as GridBoxFormatter
        val gridFormatterFactory: SudokuFormatterFactory = object: BoxFormatterFactory() {
            override val gridFormatterInstance: GridFormatting = GridBoxWithRefsFormatter(gridBoxFormatter)
        }
//    val gridFormatterWithCandidatesFactory: GridFormatterFactory = object: BoxFormatterFactory() {
//        override val cellValueFormatterInstance: CellValueFormatter = CellValueWithCandidatesFormatter()
//        override val cellFormatterInstance: CellFormatter = CellBoxFormatter(cellValueFormatterInstance)
//        override val gridFormatterInstance: GridFormatter = GridWithRefsBoxFormatter(gridBoxFormatter)
//    }
//    val gridBoxFormatter = BoxFormatterFactory.factoryInstance.gridBoxFormatterInstance
//    val gridWithRefsBoxFormatter = GridWithRefsBoxFormatter(gridBoxFormatter)
        val grids = listOf(
                Grid.GridBuilder(2).build(), Grid.GridBuilder(3).build(), Grid.GridBuilder(4).build(), Grid.GridBuilder(5).build(), Grid.GridBuilder(6).build())
        grids.forEach { grid ->
            grid.cellList[0].cellValue.setValue(1)
            println("\n\n\ngridSize: ${grid.gridSize}")
            println(gridFormatterFactory.gridFormatterInstance.format(grid))
//        println()
//        println()
//        println(gridFormatterWithCandidatesFactory.gridFormatterInstance.format(grid) + "\n")
        }
    }
}