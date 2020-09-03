package nl.jhvh.sudoku.grid.model

import nl.jhvh.sudoku.grid.model.Grid.GridBuilder
import nl.jhvh.sudoku.grid.model.cell.CellRef
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.random.Random

/** Unit tests for [GridBuilder] */
internal class GridBuilderTest {

    @Test
    fun constructor() {
        // TODO
    }

    @Test
    fun isBuilt() {
        val blockSize = 3
        val subject: GridBuilder = GridBuilder(blockSize)
        // TODO
    }

    @Test
    fun build() {
        val blockSize = 4
        val subject: GridBuilder = GridBuilder(blockSize)
        // TODO
    }

    @Test
    fun `fix by String cellRef - regular values`() {
        // given
        val blockSize = 5
        val subject: GridBuilder = GridBuilder(blockSize)
        val expected: MutableMap<CellRef, Int> = mutableMapOf()

        // when
        subject.fix("A2", 4)
        expected[CellRef("A2")] = 4

        subject.fix("x14", 9)
        expected[CellRef("X14")] = 9

        subject.fix(" p 8", 24)
        expected[CellRef("P8")] = 24

        subject.fix(" r 2 ", 1)
        expected[CellRef("R2")] = 1

        // then
        // Grid constructor is private, so even if mocked we can not verify the constructor call
        // so we have to build the real Grid
        val grid = subject.build()
        assertThat(grid.fixedValues).isEqualTo(expected)
    }

    @Test
    fun `fix by CellRef`() {
        val blockSize = 4
        val subject: GridBuilder = GridBuilder(blockSize)
    }

    @Test
    fun `fixCell - coordinates outside of Grid`() {
        val blockSize = 4
        val gridSize = blockSize * blockSize
        var gridBuilder: GridBuilder

        // edge values OK
        gridBuilder = GridBuilder(blockSize)
        gridBuilder.fix(CellRef(0, 0), Random.nextInt(1, gridSize))

        gridBuilder = GridBuilder(blockSize)
        gridBuilder.fix(CellRef(0, gridSize-1), Random.nextInt(1, gridSize))

        gridBuilder = GridBuilder(blockSize)
        gridBuilder.fix(CellRef(gridSize-1, 0), Random.nextInt(1, gridSize))

        gridBuilder = GridBuilder(blockSize)
        gridBuilder.fix(CellRef(gridSize-1, gridSize-1), Random.nextInt(1, gridSize))

        // Outside Grid - exception
        gridBuilder = GridBuilder(blockSize)
        assertThrows<IllegalArgumentException> { gridBuilder.fix(CellRef(gridSize, 0), Random.nextInt(1, gridSize)) }

        gridBuilder = GridBuilder(blockSize)
        assertThrows<IllegalArgumentException> { gridBuilder.fix(CellRef(-1, 0), Random.nextInt(1, gridSize)) }

        gridBuilder = GridBuilder(blockSize)
        assertThrows<IllegalArgumentException> { gridBuilder.fix(CellRef(0, gridSize), Random.nextInt(1, gridSize)) }

        gridBuilder = GridBuilder(blockSize)
        assertThrows<IllegalArgumentException> { gridBuilder.fix(CellRef(0, -1), Random.nextInt(1, gridSize)) }
    }

}
