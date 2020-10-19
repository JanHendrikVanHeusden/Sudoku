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