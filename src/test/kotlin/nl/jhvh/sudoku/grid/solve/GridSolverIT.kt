package nl.jhvh.sudoku.grid.solve

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

import nl.jhvh.sudoku.grid.model.Grid
import org.junit.jupiter.api.Test

/**
 * Class to integration-test [GridSolver] with correct (solvable) and incorrect (not solvable or multiple solutions) Sudoku puzzles.
 * > See also the README.md in this project for links to testcases, various websites, etc.
 */
class GridSolverIT {

    @Test
    fun solveGridEasy1() {
        val gridBuilder = Grid.GridBuilder()
        // build an easy to solve grid
        val grid = gridBuilder
                .fix("A1", 5)
                .fix("A2", 8)
                .fix("A3", 4)
                .fix("A4", 7)
                .fix("A5", 2)
                .fix("A6", 1)
                .fix("A7", 3)
                .fix("A8", 6)
                .fix("A9", 9)
                .fix("B1", 2)
                .fix("B2", 3)
                .fix("B5", 6)
                .fix("B6", 4)
                .fix("C3", 9)
                .fix("C5", 3)
                .fix("C6", 8)
                .fix("C7", 4)
                .fix("C8", 1)
                .fix("D1", 8)
                .fix("D2", 9)
                .fix("D3", 6)
                .fix("D5", 1)
                .fix("D6", 7)
                .fix("D8", 4)
                .fix("E3", 5)
                .fix("E5", 4)
                .fix("E6", 9)
                .fix("E8", 8)
                .fix("E9", 7)
                .fix("F1", 7)
                .fix("F2", 4)
                .fix("F3", 3)
                .fix("F5", 5)
                .fix("F6", 6)
                .fix("F8", 9)
                .fix("G3", 8)
                .fix("G5", 7)
                .fix("G6", 3)
                .fix("G7", 1)
                .fix("G8", 2)
                .fix("H1", 4)
                .fix("H2", 6)
                .fix("H5", 9)
                .fix("H6", 2)
                .fix("I1", 3)
                .fix("I2", 1)
                .fix("I3", 2)
                .fix("I4", 6)
                .fix("I5", 8)
                .fix("I6", 5)
                .fix("I7", 9)
                .fix("I8", 7)
                .fix("I9", 4)
                .build()

        grid.solveGrid()
        // todo: asserts
    }

    @Test
    fun solveGridHard1() {
        val gridBuilder = Grid.GridBuilder()
        val grid = gridBuilder
                .fix("A4", 6)
                .fix("A8", 7)
                .fix("B4", 8)
                .fix("B5", 7)
                .fix("B7", 4)
                .fix("B9", 6)
                .fix("C5", 1)
                .fix("C6", 2)
                .fix("C8", 9)
                .fix("D1", 6)
                .fix("D2", 4)
                .fix("D6", 7)
                .fix("D7", 8)
                .fix("E2", 3)
                .fix("E3", 2)
                .fix("E7", 7)
                .fix("E8", 6)
                .fix("F3", 8)
                .fix("F4", 4)
                .fix("F8", 5)
                .fix("F9", 9)
                .fix("G2", 2)
                .fix("G4", 7)
                .fix("G5", 6)
                .fix("H1", 3)
                .fix("H3", 6)
                .fix("H5", 5)
                .fix("H6", 8)
                .fix("I2", 1)
                .fix("I6", 4)
                .build()

        grid.solveGrid()
        // todo: asserts
    }

    @Test
    fun solveGridExpertLevel1() {
        val gridBuilder = Grid.GridBuilder()
        val grid = gridBuilder
                .fix("A2", 4)
                .fix("B3", 7)
                .fix("B4", 4)
                .fix("B5", 8)
                .fix("C6", 6)
                .fix("C7", 3)
                .fix("C8", 5)
                .fix("D2", 8)
                .fix("D6", 9)
                .fix("D7", 2)
                .fix("D8", 1)
                .fix("D9", 5)
                .fix("E1", 1)
                .fix("E7", 6)
                .fix("F1", 9)
                .fix("F2", 5)
                .fix("F4", 3)
                .fix("G9", 9)
                .fix("H2", 6)
                .fix("H4", 2)
                .fix("H6", 8)
                .fix("H7", 7)
                .fix("H8", 3)
                .fix("I1", 2)
                .fix("I2", 7)
                .build()

        grid.solveGrid()
        // todo: asserts
    }

    @Test
    fun `supposed Hardest Ever Sudoku`() {
        val gridBuilder = Grid.GridBuilder()
        val grid = gridBuilder
                .fix("A1", 8)
                .fix("B3", 3)
                .fix("B4", 6)
                .fix("C2", 7)
                .fix("C5", 9)
                .fix("C7", 2)
                .fix("D2", 5)
                .fix("D6", 7)
                .fix("E5", 4)
                .fix("E6", 5)
                .fix("E7", 7)
                .fix("F4", 1)
                .fix("F8", 3)
                .fix("G3", 1)
                .fix("G8", 6)
                .fix("G9", 8)
                .fix("H3", 8)
                .fix("H4", 5)
                .fix("H8", 1)
                .fix("I2", 9)
                .fix("I7", 4)
                .build()

        grid.solveGrid()
        // todo: asserts
    }

    @Test
    fun `try Hardest Sudoku Ever - Golden Nugget`() {
        val gridBuilder = Grid.GridBuilder()
        val grid = gridBuilder
                .fix("A8", 3)
                .fix("A9", 9)
                .fix("B5", 1)
                .fix("B9", 5)
                .fix("C3", 3)
                .fix("C6", 5)
                .fix("C7", 8)
                .fix("D3", 8)
                .fix("D6", 9)
                .fix("D9", 6)
                .fix("E2", 7)
                .fix("E5", 2)
                .fix("F1", 1)
                .fix("F4", 4)
                .fix("G3", 9)
                .fix("G6", 8)
                .fix("G8", 5)
                .fix("H2", 2)
                .fix("H7", 6)
                .fix("I1", 4)
                .fix("I4", 7)
                .build()

        grid.solveGrid()
        // todo: asserts
    }

}