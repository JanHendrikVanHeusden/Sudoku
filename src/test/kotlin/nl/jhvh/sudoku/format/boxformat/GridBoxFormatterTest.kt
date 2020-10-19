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

import nl.jhvh.sudoku.grid.model.Grid
import nl.jhvh.sudoku.grid.model.Grid.GridBuilder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/** Unit integration test for [Grid] formatting */
internal class GridBoxFormatterTest {

    private val formatterFactory = BoxFormatterFactory()
    private val subject = GridBoxFormatter(formatterFactory.rowFormatterInstance as RowBoxFormatter, formatterFactory.columnFormatterInstance as ColumnBoxFormatter)

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
    private lateinit var grid16: Grid

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

        grid16 = GridBuilder(4).build()
    }

    @Test
    fun format() {
        val formatted = subject.format((grid9)).toString()
        // println(formatted)
        assertThat(formatted).isEqualTo(
                """
                    ╔═══╤═══╤═══╦═══╤═══╤═══╦═══╤═══╤═══╗
                    ║►7 │   │   ║   │   │   ║   │   │   ║
                    ╟───┼───┼───╫───┼───┼───╫───┼───┼───╢
                    ║►4 │   │►2 ║   │   │   ║   │   │   ║
                    ╟───┼───┼───╫───┼───┼───╫───┼───┼───╢
                    ║   │   │   ║   │   │   ║►8 │   │   ║
                    ╠═══╪═══╪═══╬═══╪═══╪═══╬═══╪═══╪═══╣
                    ║   │   │   ║   │   │ 4 ║   │ 2 │   ║
                    ╟───┼───┼───╫───┼───┼───╫───┼───┼───╢
                    ║   │   │   ║   │   │   ║   │   │   ║
                    ╟───┼───┼───╫───┼───┼───╫───┼───┼───╢
                    ║   │   │   ║   │   │   ║   │   │   ║
                    ╠═══╪═══╪═══╬═══╪═══╪═══╬═══╪═══╪═══╣
                    ║   │   │   ║   │   │   ║   │   │   ║
                    ╟───┼───┼───╫───┼───┼───╫───┼───┼───╢
                    ║   │   │   ║   │   │   ║   │   │   ║
                    ╟───┼───┼───╫───┼───┼───╫───┼───┼───╢
                    ║   │   │   ║   │   │►3 ║   │   │   ║
                    ╚═══╧═══╧═══╩═══╧═══╧═══╩═══╧═══╧═══╝
                """.tidy())
    }

    @Test
    fun nakedFormat() {
        val formatted = subject.nakedFormat((grid9)).toString()
        // println(formatted)
        assertThat(formatted).isEqualTo(
                """
                    ►7 │   │   ║   │   │   ║   │   │   
                    ───┼───┼───╫───┼───┼───╫───┼───┼───
                    ►4 │   │►2 ║   │   │   ║   │   │   
                    ───┼───┼───╫───┼───┼───╫───┼───┼───
                       │   │   ║   │   │   ║►8 │   │   
                    ═══╪═══╪═══╬═══╪═══╪═══╬═══╪═══╪═══
                       │   │   ║   │   │ 4 ║   │ 2 │   
                    ───┼───┼───╫───┼───┼───╫───┼───┼───
                       │   │   ║   │   │   ║   │   │   
                    ───┼───┼───╫───┼───┼───╫───┼───┼───
                       │   │   ║   │   │   ║   │   │   
                    ═══╪═══╪═══╬═══╪═══╪═══╬═══╪═══╪═══
                       │   │   ║   │   │   ║   │   │   
                    ───┼───┼───╫───┼───┼───╫───┼───┼───
                       │   │   ║   │   │   ║   │   │   
                    ───┼───┼───╫───┼───┼───╫───┼───┼───
                       │   │   ║   │   │►3 ║   │   │   
                """.tidy())
    }

    @Test
    fun getLeftBorder() {
        val formatted = subject.getLeftBorder((grid9)).toString()
        // println(formatted)
        assertThat(formatted).isEqualTo(
                """
                    ║
                    ╟
                    ║
                    ╟
                    ║
                    ╠
                    ║
                    ╟
                    ║
                    ╟
                    ║
                    ╠
                    ║
                    ╟
                    ║
                    ╟
                    ║
                """.tidy())
    }

    @Test
    fun getRightBorder() {
        val formatted = subject.getRightBorder((grid9)).toString()
        // println(formatted)
        assertThat(formatted).isEqualTo(
                """
                    ║
                    ╢
                    ║
                    ╢
                    ║
                    ╣
                    ║
                    ╢
                    ║
                    ╢
                    ║
                    ╣
                    ║
                    ╢
                    ║
                    ╢
                    ║
                """.tidy())
    }

    @Test
    fun getTopBorder() {
        val formatted = subject.getTopBorder((grid9)).toString()
        // println(formatted)
        assertThat(formatted).isEqualTo("═══╤═══╤═══╦═══╤═══╤═══╦═══╤═══╤═══")
    }

    @Test
    fun getBottomBorder() {
        val formatted = subject.getBottomBorder((grid9)).toString()
        // println(formatted)
        assertThat(formatted).isEqualTo("═══╧═══╧═══╩═══╧═══╧═══╩═══╧═══╧═══")
    }

    @Test
    fun getTopLeftEdge() {
        val formatted = subject.getTopLeftEdge((grid9))
        // println(formatted)
        assertThat(formatted).isEqualTo("╔")
    }

    @Test
    fun getTopRightEdge() {
        val formatted = subject.getTopRightEdge((grid9))
        // println(formatted)
        assertThat(formatted).isEqualTo("╗")
    }

    @Test
    fun getBottomLeftEdge() {
        val formatted = subject.getBottomLeftEdge((grid9))
        // println(formatted)
        assertThat(formatted).isEqualTo("╚")
    }

    @Test
    fun getBottomRightEdge() {
        val formatted = subject.getBottomRightEdge((grid9))
        // println(formatted)
        assertThat(formatted).isEqualTo("╝")
    }
}
