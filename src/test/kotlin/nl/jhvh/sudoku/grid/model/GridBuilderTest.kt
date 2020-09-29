package nl.jhvh.sudoku.grid.model

import io.mockk.every
import io.mockk.mockkConstructor
import io.mockk.spyk
import io.mockk.unmockkConstructor
import io.mockk.verify
import nl.jhvh.sudoku.base.MAX_BLOCK_SIZE
import nl.jhvh.sudoku.grid.model.Grid.GridBuilder
import nl.jhvh.sudoku.grid.model.cell.CellRef
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.random.Random
import kotlin.test.assertFailsWith

/** Unit tests for [GridBuilder] */
internal class GridBuilderTest {

    @Test
    fun `GridBuilder constructor`() {
        // regular block sizes
        var blockSize = 2
        var builder = GridBuilder(blockSize) // OK
        assertThat(builder.blockSize).isEqualTo(blockSize)
        assertThat(builder.isBuilt).isFalse()

        blockSize = 3
        builder = GridBuilder() // default 3
        assertThat(builder.blockSize).isEqualTo(blockSize)
        assertThat(builder.isBuilt).isFalse()

        blockSize = 3
        builder = GridBuilder(3)
        assertThat(builder.blockSize).isEqualTo(blockSize)
        assertThat(builder.isBuilt).isFalse()

        blockSize = 10
        builder = GridBuilder(blockSize)
        assertThat(builder.blockSize).isEqualTo(blockSize)
        assertThat(builder.isBuilt).isFalse()

        blockSize = MAX_BLOCK_SIZE
        builder = GridBuilder(blockSize)
        assertThat(builder.blockSize).isEqualTo(blockSize)
        assertThat(builder.isBuilt).isFalse()

        // Invalid block sizes
        assertFailsWith<IllegalArgumentException> {GridBuilder(MAX_BLOCK_SIZE+1) }
        assertFailsWith<IllegalArgumentException> {GridBuilder(MAX_BLOCK_SIZE+100) }
        assertFailsWith<IllegalArgumentException> {GridBuilder(-1) }
        assertFailsWith<IllegalArgumentException> {GridBuilder(0) }
        assertFailsWith<IllegalArgumentException> {GridBuilder(1) }
        assertFailsWith<IllegalArgumentException> {GridBuilder(Int.MAX_VALUE) }
        assertFailsWith<IllegalArgumentException> {GridBuilder(Int.MIN_VALUE) }
    }

    @Test
    fun isBuilt() {
        // given
        val subject: GridBuilder = GridBuilder(2)
        assertThat(subject.isBuilt).isFalse()
        // when
        subject.build()
        // then
        assertThat(subject.isBuilt).isTrue()
    }

    @Test
    fun `Trying to reuse a GridBuilder should fail`() {
        val blockSize = 3
        val subject = GridBuilder(blockSize)
        assertThat(subject.build()).isNotNull
        assertFailsWith<IllegalStateException> { subject.build() }
    }

    @Test
    fun build() {
        // given
        val blockSize = 4
        val gridToString = "mocked Grid"
        try {
            mockkConstructor(Grid::class)
            every { anyConstructed<Grid>().toString() } returns gridToString
            // when
            val subject: GridBuilder = GridBuilder(blockSize)
            // then
            assertThat(subject.isBuilt).isFalse()
            val grid = subject.build()
            assertThat(grid.toString()).isEqualTo(gridToString)
            assertThat(subject.isBuilt).isTrue()
        } finally {
            unmockkConstructor(Grid::class)
        }
    }

    @Test
    fun `build should fail on invalid block sizes`() {
        // Regular values
        GridBuilder().build() // default = 3
        var blockSize = 2
        GridBuilder(blockSize).build()
        blockSize = 10
        GridBuilder(blockSize).build()

        // out of range
        blockSize = MAX_BLOCK_SIZE+1
        assertFailsWith<IllegalArgumentException> { GridBuilder(blockSize).build() }
        blockSize = MAX_BLOCK_SIZE+100
        assertFailsWith<IllegalArgumentException> { GridBuilder(blockSize).build() }
        blockSize = 0
        assertFailsWith<IllegalArgumentException> { GridBuilder(blockSize).build() }
        blockSize = -1
        assertFailsWith<IllegalArgumentException> { GridBuilder(blockSize).build() }
        blockSize = Int.MAX_VALUE
        assertFailsWith<IllegalArgumentException> { GridBuilder(blockSize).build() }
        blockSize = Int.MIN_VALUE
        assertFailsWith<IllegalArgumentException> { GridBuilder(blockSize).build() }

    }

    @Test
    fun `fix - out of range values`() {
        // given
        val blockSize = 5
        val subject: GridBuilder = GridBuilder(blockSize)
        val expected: MutableMap<CellRef, Int> = mutableMapOf()

        // regular values
        val cellRef1 = CellRef("A2")
        subject.fix(cellRef1, 1)
        expected[cellRef1] = 1
        subject.fix("X14", 25)
        val cellRef2 = CellRef("X14")
        expected[cellRef2] = 25

        // out of range
        assertFailsWith<java.lang.IllegalArgumentException> { subject.fix(CellRef(" p 8"), 26) }
        assertFailsWith<java.lang.IllegalArgumentException> { subject.fix(CellRef(" r 2 "), 0) }
        assertFailsWith<java.lang.IllegalArgumentException> { subject.fix(CellRef(" C2"), Int.MAX_VALUE) }
        assertFailsWith<java.lang.IllegalArgumentException> { subject.fix(CellRef(" F4 "), -1) }
        assertFailsWith<java.lang.IllegalArgumentException> { subject.fix(CellRef("K14"), Int.MIN_VALUE) }

        // then
        // Grid constructor is private, so even if mocked we can not verify the constructor call
        // so we have to build the real Grid
        val grid = subject.build()
        assertThat(grid.fixedValues).isEqualTo(expected)
    }

    @Test
    fun `fix - regular values`() {
        // given
        val blockSize = 5
        val subject: GridBuilder = GridBuilder(blockSize)
        val expected: MutableMap<CellRef, Int> = mutableMapOf()

        // when
        val cellRef1 = CellRef("A2")
        subject.fix(cellRef1, 4)
        expected[cellRef1] = 4

        val cellRef2 = CellRef("X14")
        subject.fix(cellRef2, 9)
        expected[cellRef2] = 9

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
    fun `fix by String cellRef`() {
        val blockSize = 4
        val subject: GridBuilder = spyk(GridBuilder(blockSize))
        subject.fix("A5", 10)
        val cellRef = CellRef("A5")
        verify { subject.fix(cellRef, 10) }
    }

    @Test
    fun `fixCell should fail when coordinates are outside of the Grid`() {
        val blockSize = 4
        val gridSize = blockSize * blockSize
        var gridBuilder: GridBuilder

        // edge values OK
        gridBuilder = GridBuilder(blockSize)
        gridBuilder.fix(CellRef(0, 0), Random.nextInt(1, gridSize))

        gridBuilder = GridBuilder(blockSize)
        gridBuilder.fix(CellRef(0, gridSize - 1), Random.nextInt(1, gridSize))

        gridBuilder = GridBuilder(blockSize)
        gridBuilder.fix(CellRef(gridSize - 1, 0), Random.nextInt(1, gridSize))

        gridBuilder = GridBuilder(blockSize)
        gridBuilder.fix(CellRef(gridSize - 1, gridSize - 1), Random.nextInt(1, gridSize))

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

    @Test
    fun `fixCell should fail when the Grid was built already`() {
        // given
        val blockSize = 5
        val subject = GridBuilder(blockSize)
        subject.fix("C8", 2)
        subject.fix("A14", 25)
        val grid = subject.build()
        assertThat(grid.findCell("C8").isFixed).isTrue()
        assertThat(grid.findCell("A14").isFixed).isTrue()
        assertThat(grid.findCell("B8").isFixed).isFalse()
        assertThat(grid.findCell("F11").isFixed).isFalse()
        // when, then
        assertFailsWith<IllegalStateException> { subject.fix("C8", 2) }
        assertFailsWith<IllegalStateException> { subject.fix("F11", 15) }
    }
}
