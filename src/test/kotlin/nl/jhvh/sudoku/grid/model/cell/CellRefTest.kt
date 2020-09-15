package nl.jhvh.sudoku.grid.model.cell

import nl.jhvh.sudoku.grid.model.cell.CellRef.CellRefCalculation.colRefToIndex
import nl.jhvh.sudoku.grid.model.cell.CellRef.CellRefCalculation.getColRefFromCellRef
import nl.jhvh.sudoku.grid.model.cell.CellRef.CellRefCalculation.getRowRefFromCellRef
import nl.jhvh.sudoku.grid.model.cell.CellRef.CellRefCalculation.indexToColRef
import nl.jhvh.sudoku.grid.model.cell.CellRef.CellRefCalculation.indexToRowRef
import nl.jhvh.sudoku.grid.model.cell.CellRef.CellRefCalculation.rowRefToIndex
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

/**
 * Unit tests for [CellRef] including it's companion object [CellRef.CellRefCalculation].
 *
 * It should be possible to verify / mock / spyk the companion object, but could not get it working.
 * So the constructor tests are not strictly isolated unit tests, as they include calls to other non-private methods.
 */
internal class CellRefTest {

    @Test
    fun `test CellRef(x,y) constructor`() {
        // given
        val x = 1 // zero based
        val y = 10 // zero based
        // when
        val cellRef = CellRef(x, y)
        // then
        assertThat(cellRef.x).isEqualTo(x)
        assertThat(cellRef.y).isEqualTo(y)
        // had no success with mocking or verifying companion object
        // so just asserting the results, including the results of all in-between calls
        assertThat(cellRef.colRef).isEqualTo("2") // 1 based
        assertThat(cellRef.rowRef).isEqualTo("K")
        assertThat(cellRef.cellRef).isEqualTo("K2")
    }

    @Test
    fun `test CellRef(String) constructor`() {
        // given
        val rowRef = "B"
        val colRef = "5"
        // when
        with(CellRef("$rowRef$colRef")) { // B5
            // then - had no success with mocking or verifying companion object
            // so just asserting the results, including the results of all in-between calls
            assertThat(this.x).isEqualTo(4) // zero based
            assertThat(this.y).isEqualTo(1) // zero based
            assertThat(this.colRef).isEqualTo(colRef) // 1 based
            assertThat(this.rowRef).isEqualTo(rowRef)
            assertThat(this.cellRef).isEqualTo(rowRef + colRef)
        }
        // lower case, whitespace in between
        with(CellRef(" b 5 ")) {
            assertThat(this.x).isEqualTo(4) // zero based
            assertThat(this.y).isEqualTo(1) // zero based
            assertThat(this.colRef).isEqualTo(colRef) // 1 based
            assertThat(this.rowRef).isEqualTo(rowRef)
            assertThat(this.cellRef).isEqualTo(rowRef + colRef)
        }
    }

    @Test
    fun `test CellRef(String, String) constructor`() {
        // given
        val cellRef = " D8 \t"
        // when
        with(CellRef(cellRef)) {
            // then - had no success with mocking or verifying companion object
            // so just asserting the results, including the results of all in-between calls
            assertThat(this.x).isEqualTo(7)
            assertThat(this.y).isEqualTo(3)
            assertThat(this.colRef).isEqualTo("8") // 1 based
            assertThat(this.rowRef).isEqualTo("D")
            assertThat(this.cellRef).isEqualTo("D8")
        }
        // lower case
        with(CellRef(cellRef.toLowerCase())) {
            assertThat(this.x).isEqualTo(7)
            assertThat(this.y).isEqualTo(3)
            assertThat(this.colRef).isEqualTo("8") // 1 based
            assertThat(this.rowRef).isEqualTo("D")
            assertThat(this.cellRef).isEqualTo("D8")
        }
    }

    @Test
    fun `test CellRef constructors - larger coordinate values`() {
        // had no success with mocking or verifying companion object
        // so just asserting the results, including the results of all in-between calls

        // given, when, then
        assertThat(CellRef(0, 25).rowRef).isEqualTo("Z")
        assertThat(CellRef(0, 26).rowRef).isEqualTo("AA")
        // given, when
        with (CellRef(80, 49)) {
            // then:  790 = ADJ (1 based), so 789 zero based -> ADJ
            assertThat(this.rowRef).isEqualTo("AX")
            assertThat(this.colRef).isEqualTo("81")
            assertThat(this.cellRef).isEqualTo("AX81")
        }
        // given, when, then
        assertThat(CellRef(80, 49).rowRef).isEqualTo("AX")
        // given
        for (y in 25..99) {
            // when
            val rowRef = CellRef(0, y).rowRef
            val yFromRef1 = CellRef(rowRef = rowRef, colRef = "1").y
            val yFromRef2 = CellRef(cellRef = "${rowRef}1").y
            // then
            assertThat(yFromRef2)
                    .isEqualTo(y)
                    .isEqualTo(yFromRef1)
            if ((y+1) % 26 == 0) {
                assertThat(rowRef)
                        .`as`("Test fails for y=$y")
                        .endsWith("Z")
            }
            if ((y+1) % 26 == 0) {
                assertThat(rowRef)
                        .`as`("Test fails for y=$y")
                        .endsWith("Z")
            }
            if ((y+1) % 26 == 1) {
                assertThat(rowRef)
                        .`as`("Test fails for y=$y")
                        .endsWith("A")
            }
        }
    }

    @Test
    fun `test CellRef constructors - illegal input`() {
        // had no success with mocking or verifying companion object
        // so just asserting the results, including the results of all in-between calls

        assertThat(CellRef(0,0)).isNotNull

        // Negative indices
        with(assertFailsWith(IllegalArgumentException::class) {CellRef(-1, 10)}) {
            assertThat(this.message).isEqualTo("Negative column index [-1] is not allowed")
        }
        with(assertFailsWith(IllegalArgumentException::class) {CellRef(Int.MIN_VALUE, 10)}) {
            assertThat(this.message).isEqualTo("Negative column index [${Int.MIN_VALUE}] is not allowed")
        }
        with(assertFailsWith(IllegalArgumentException::class) {CellRef("B", "-1")}) {
            assertThat(this.message).startsWith("Invalid format, can not parse column reference [-1]")
        }
        with(assertFailsWith(IllegalArgumentException::class) {CellRef("B-1")}) {
            assertThat(this.message).startsWith("Invalid format, can not parse cell reference [B-1]")
        }
        with(assertFailsWith(IllegalArgumentException::class) {CellRef(1, -1)}) {
            assertThat(this.message).isEqualTo("Negative row index [-1] is not allowed")
        }
        with(assertFailsWith(IllegalArgumentException::class) {CellRef(1, Int.MIN_VALUE)}) {
            assertThat(this.message).isEqualTo("Negative row index [${Int.MIN_VALUE}] is not allowed")
        }

        val maxBlockSize = 100
        assertThat(CellRef(maxBlockSize-1,maxBlockSize-1)).isNotNull

        // Too large indices
        with(assertFailsWith(IllegalArgumentException::class) {CellRef(maxBlockSize, 10)}) {
            assertThat(this.message).isEqualTo(
                    "Too high value [100] for column index! Block size asked for is higher than 100")
        }
        with(assertFailsWith(IllegalArgumentException::class) {CellRef(10, maxBlockSize)}) {
            assertThat(this.message).isEqualTo(
                    "Too high value [100] for row index! Block size asked for is higher than 100")
        }
        // println(indexToRowRef(maxBlockSize-1))
        val maxRowRef = "CV" // corresponds with max block size = 100, should be ok        assertThat(CellRef(maxRowRef, "1")).isNotNull
        val tooHighRowRef = "CW" // rowRefMaxBlockSize + 1; should fail
        with(assertFailsWith(IllegalArgumentException::class) {CellRef(tooHighRowRef, "67")}) {
            assertThat(this.message).isEqualTo(
                    "Too high value [$tooHighRowRef] for row reference! Block size asked for is [101] or higher but must be between 1 and 100")
        }
        with(assertFailsWith(IllegalArgumentException::class) {CellRef("${tooHighRowRef}88")}) {
            assertThat(this.message).isEqualTo(
                    "Too high value [$tooHighRowRef] for row reference! Block size asked for is [101] or higher but must be between 1 and 100")
        }
        with(assertFailsWith(IllegalArgumentException::class) {CellRef("skjhahakjajkajhuihsajnshynmklsjfgtio88")}) {
            assertThat(this.message).isEqualTo(
                    "Too high value [SKJHAHAKJAJKAJHUIHSAJNSHYNMKLSJFGTIO] for row reference! Block size asked for is [101] or higher but must be between 1 and 100")
        }
        with(assertFailsWith(IllegalArgumentException::class) {CellRef("x", "${maxBlockSize+1}")}) {
            assertThat(this.message).isEqualTo(
                    "Too high value [${maxBlockSize+1}] for column reference! Block size asked for is [101] or higher but must be between 1 and 100")
        }

        // empty or blank references
        with(assertFailsWith(IllegalArgumentException::class) {CellRef("", "67")}) {
            assertThat(this.message).startsWith("Row reference must not be blank.")
        }
        with(assertFailsWith(IllegalArgumentException::class) {CellRef(" \t\n", "14")}) {
            assertThat(this.message).startsWith("Row reference must not be blank.")
        }
        with(assertFailsWith(IllegalArgumentException::class) {CellRef("A", "")}) {
            assertThat(this.message).startsWith("Column reference must not be blank.")
        }
        with(assertFailsWith(IllegalArgumentException::class) {CellRef("D", " \n\t")}) {
            assertThat(this.message).startsWith("Column reference must not be blank.")
        }
        // incomplete, bogus
        with(assertFailsWith(IllegalArgumentException::class) {CellRef(cellRef = "D")}) {
            assertThat(this.message).startsWith("Invalid format, can not parse cell reference [D].")
        }
        with(assertFailsWith(IllegalArgumentException::class) {CellRef(cellRef = "1")}) {
            assertThat(this.message).startsWith("Invalid format, can not parse cell reference [1].")
        }
        with(assertFailsWith(IllegalArgumentException::class) {CellRef("D", "#1")}) {
            assertThat(this.message).startsWith("Invalid format, can not parse column reference [#1].")
        }
        with(assertFailsWith(IllegalArgumentException::class) {CellRef("F#", "1")}) {
            assertThat(this.message).startsWith("Invalid format, can not parse row reference [F#].")
        }
    }

        @Test
        fun `test testHashCode`() {
            with (CellRef(8, 11)) {
                assertThat(this.hashCode())
                        // stays the same when calculated again
                        .isEqualTo(hashCode())
                        .isEqualTo(CellRef(8, 11).hashCode())
            }
            val expected = CellRef("CU23")
            with (CellRef(22, 98)) {
                assertThat(this.hashCode())
                        .`as`("hashCode() should be equal! actual = $this, expected = $expected")
                        .isEqualTo(expected.hashCode())
                        .`as`("hashCode() should be equal! actual = $this, expected = $expected")
                        .isEqualTo(CellRef("cu23").hashCode())
                        .`as`("hashCode() should be equal! actual = $this, expected = $expected")
                        .isEqualTo(CellRef("CU", "23").hashCode())
                        .`as`("hashCode() should be equal! actual = $this, expected = $expected")
                        .isEqualTo(CellRef("cU", "23").hashCode())
            }
        }

    @Test
    fun `test testEquals`() {
        with (CellRef(3, 14)) {
            assertThat(this)
                    // equal to itself
                    .isEqualTo(this)
                    .isEqualTo(CellRef(3, 14))
        }
        with (CellRef(98, 78)) {
            assertThat(this)
                    .isEqualTo(CellRef("CA99"))
                    .isEqualTo(CellRef("Ca99"))
                    .isEqualTo(CellRef("CA", "99"))
                    .isEqualTo(CellRef("cA", "99"))
        }
    }

    @Test
    fun `test testToString`() {
        with (CellRef(15, 89).toString()) {
            assertThat(this)
                    .startsWith("CellRef ")
                    .contains("x=15", "y=89", "rowRef='CL'", "colRef='16'", "cellRef='CL16'")
        }
    }

    @Test
    fun`test CellRefCalculation - getRowRefFromCellRef`() {
        assertThat(getRowRefFromCellRef(" A 10 \t")).isEqualTo("A")
        assertThat(getRowRefFromCellRef("F28")).isEqualTo("F")
        assertThat(getRowRefFromCellRef("abcdeGHIjklmmnoujiaoe123456789102345678")).isEqualTo("ABCDEGHIJKLMMNOUJIAOE")

        with(assertFailsWith(IllegalArgumentException::class, { getRowRefFromCellRef("A 1 2") })) {
            assertThat(this.message).startsWith("Invalid format, can not parse cell reference [A 1 2].")
        }
        with(assertFailsWith(IllegalArgumentException::class, { getRowRefFromCellRef("123456789102345678") })) {
            assertThat(this.message).startsWith("Invalid format, can not parse cell reference [123456789102345678].")
        }
        assertThat(getRowRefFromCellRef("abcdeGHIjklmmnoujiaoe123456789102345678")).isEqualTo("ABCDEGHIJKLMMNOUJIAOE")
        with(assertFailsWith(IllegalArgumentException::class, { getRowRefFromCellRef("123456789102345678") })) {
            assertThat(this.message).startsWith("Invalid format, can not parse cell reference [123456789102345678].")
        }
        with(assertFailsWith(IllegalArgumentException::class, { getRowRefFromCellRef("A.12") })) {
            assertThat(this.message).startsWith("Invalid format, can not parse cell reference [A.12].")
        }
    }

    @Test
    fun`test CellRefCalculation - getColRefFromCellRef`() {
        assertThat(getColRefFromCellRef(" X 14 \t")).isEqualTo("14")
        assertThat(getColRefFromCellRef("BD 10")).isEqualTo("10")
        assertThat(getColRefFromCellRef("abcdeGHIjklmmnoujiaoe123456789102345678")).isEqualTo("123456789102345678")

        assertThat(getColRefFromCellRef("abcdeGHIjklmmnoujiaoe 123456789102345678")).isEqualTo("123456789102345678")
        with(assertFailsWith(IllegalArgumentException::class, { getColRefFromCellRef("BD 1.0") })) {
            assertThat(this.message).startsWith("Invalid format, can not parse cell reference [BD 1.0].")
        }
        with(assertFailsWith(IllegalArgumentException::class, { getColRefFromCellRef("123456789102345678") })) {
            assertThat(this.message).startsWith("Invalid format, can not parse cell reference [123456789102345678].")
        }
        with(assertFailsWith(IllegalArgumentException::class, { getColRefFromCellRef("A.12") })) {
            assertThat(this.message).startsWith("Invalid format, can not parse cell reference [A.12].")
        }
        with(assertFailsWith(IllegalArgumentException::class, { getColRefFromCellRef("A1,2") })) {
            assertThat(this.message).startsWith("Invalid format, can not parse cell reference [A1,2].")
        }
        with(assertFailsWith(IllegalArgumentException::class, { getColRefFromCellRef("A1.2") })) {
            assertThat(this.message).startsWith("Invalid format, can not parse cell reference [A1.2].")
        }
    }

    @Test
    fun`test CellRefCalculation - rowRefToIndex`() {
        assertThat(rowRefToIndex("A")).isEqualTo(0)
        assertThat(rowRefToIndex("X")).isEqualTo(23)
        assertThat(rowRefToIndex(" \tB\n")).isEqualTo(1)
        assertThat(rowRefToIndex("AA")).isEqualTo(26)
        assertThat(rowRefToIndex("AB")).isEqualTo(27)

        val maxBlockSize = 100
        // println(indexToRowRef(maxBlockSize-1))
        val maxRowRef = "CV" // causes the max block size = 100, should be ok
        val tooHighRowRef = "CW" // rowRefMaxBlockSize + 1; should fail
        assertThat(rowRefToIndex(maxRowRef)).isEqualTo(maxBlockSize-1) // rowIndex is zero based
        with(assertFailsWith(IllegalArgumentException::class, { rowRefToIndex(tooHighRowRef) })) {
            assertThat(this.message).isEqualTo("Too high value [CW] for row reference! Block size asked for is [101] or higher but must be between 1 and 100")
        }
        with(assertFailsWith(IllegalArgumentException::class, { rowRefToIndex("ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZ") })) {
            assertThat(this.message).isEqualTo("Too high value [ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZ] for row reference! Block size asked for is [101] or higher but must be between 1 and 100")
        }
        with(assertFailsWith(IllegalArgumentException::class, { rowRefToIndex("A A") })) {
            assertThat(this.message).startsWith("Invalid format, can not parse row reference [A A].")
        }
        with(assertFailsWith(IllegalArgumentException::class, { rowRefToIndex("1 1") })) {
            assertThat(this.message).startsWith("Invalid format, can not parse row reference [1 1].")
        }
    }

    @Test
    fun`test CellRefCalculation - indexToRowRef`() {
        assertThat(indexToRowRef(0)).isEqualTo("A")
        assertThat(indexToRowRef(25)).isEqualTo("Z")
        assertThat(indexToRowRef(26)).isEqualTo("AA")
        val maxBlockSize = 100
        val maxRowRef = "CV" // corresponds with max block size = 100, should be ok
        assertThat(indexToRowRef(maxBlockSize-1)).isEqualTo(maxRowRef)

        with(assertFailsWith(IllegalArgumentException::class, { indexToRowRef(maxBlockSize) })) {
            assertThat(this.message).isEqualTo("Too high value [100] for row index! Block size asked for is higher than 100")
        }
        with(assertFailsWith(IllegalArgumentException::class, { indexToRowRef(Int.MAX_VALUE) })) {
            assertThat(this.message).isEqualTo("Too high value [${Int.MAX_VALUE}] for row index! Block size asked for is higher than 100")
        }
        with(assertFailsWith(IllegalArgumentException::class, { indexToRowRef(-1) })) {
            assertThat(this.message).isEqualTo("Negative row index [-1] is not allowed")
        }
        with(assertFailsWith(IllegalArgumentException::class, { indexToRowRef(Int.MIN_VALUE) })) {
            assertThat(this.message).isEqualTo("Negative row index [${Int.MIN_VALUE}] is not allowed")
        }
    }

    @Test
    fun`test CellRefCalculation - colRefToIndex`() {
        assertThat(colRefToIndex("1")).isEqualTo(0)
        assertThat(colRefToIndex("24")).isEqualTo(23)
        assertThat(colRefToIndex(" \t2\n")).isEqualTo(1)
        assertThat(colRefToIndex("99")).isEqualTo(98)

        val maxBlockSize = 100
        assertThat(colRefToIndex("$maxBlockSize")).isEqualTo(maxBlockSize-1)

        with(assertFailsWith(IllegalArgumentException::class, { colRefToIndex("${maxBlockSize+1}") })) {
            assertThat(this.message).isEqualTo("Too high value [101] for column reference! Block size asked for is [101] or higher but must be between 1 and 100")
        }
        with(assertFailsWith(IllegalArgumentException::class, { colRefToIndex("1345865952315245454454544234545445412789798464662126") })) {
            assertThat(this.message).isEqualTo("Too high value [1345865952315245454454544234545445412789798464662126] for column reference! Block size asked for is [101] or higher but must be between 1 and 100")
        }
        with(assertFailsWith(IllegalArgumentException::class, { colRefToIndex("A A") })) {
            assertThat(this.message).startsWith("Invalid format, can not parse column reference [A A].")
        }
    }

    @Test
    fun`test CellRefCalculation - indexToColRef`() {
        for (colIndex in 0..99) {
            assertThat(indexToColRef(colIndex)).isEqualTo((colIndex+1).toString())
        }
        val maxBlockSize = 100
        assertThat(indexToColRef(maxBlockSize-1)).isEqualTo(maxBlockSize.toString())

        with(assertFailsWith(IllegalArgumentException::class, { indexToColRef(maxBlockSize) })) {
            assertThat(this.message).isEqualTo("Too high value [100] for column index! Block size asked for is higher than 100")
        }
        with(assertFailsWith(IllegalArgumentException::class, { indexToColRef(Int.MAX_VALUE) })) {
            assertThat(this.message).isEqualTo("Too high value [${Int.MAX_VALUE}] for column index! Block size asked for is higher than 100")
        }
        with(assertFailsWith(IllegalArgumentException::class, { indexToColRef(-1) })) {
            assertThat(this.message).isEqualTo("Negative column index [-1] is not allowed")
        }
        with(assertFailsWith(IllegalArgumentException::class, { indexToColRef(Int.MIN_VALUE) })) {
            assertThat(this.message).isEqualTo("Negative column index [${Int.MIN_VALUE}] is not allowed")
        }
    }

}
