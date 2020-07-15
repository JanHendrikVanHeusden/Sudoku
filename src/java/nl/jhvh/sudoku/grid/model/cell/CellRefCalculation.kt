package nl.jhvh.sudoku.grid.model.cell

import java.math.BigInteger
import java.util.regex.Matcher
import java.util.regex.Pattern

const val A: Char = 'A'
const val rowGroupInCellPattern: Int = 1
const val colGroupInCellPattern: Int = 2

private const val ROW_REF_PATTERN_STRING: String = "[A-Z]+"
private val rowRefPattern = Pattern.compile("^$ROW_REF_PATTERN_STRING$")

private const val COL_REF_PATTERN_STRING = "[1-9][0-9]*"
private val colRefPattern = Pattern.compile("^$COL_REF_PATTERN_STRING$")

val cellRefPattern: Pattern = Pattern.compile("^($ROW_REF_PATTERN_STRING)\\s*($COL_REF_PATTERN_STRING)$")
val INTEGER_MAX_VALUE: BigInteger = BigInteger.valueOf(Int.MAX_VALUE.toLong())

fun parseCellRef(cellRef: String): Matcher {
    val cellRefMatcher = cellRefPattern.matcher(cellRef.trim { it <= ' ' }.toUpperCase())
    require(cellRefMatcher.find()) { "Invalid format, can not parse cell reference [$cellRef].\nFormat must be \"A1\", \"J12\", \"AC23\" etc." }
    return cellRefMatcher
}

fun getRowFromCellRef(cellRef: String): String {
    return parseCellRef(cellRef).group(rowGroupInCellPattern)
}

fun getColFromCellRef(cellRef: String): String {
    return parseCellRef(cellRef).group(colGroupInCellPattern)
}

fun rowRefToIndex(rowRef: String): Int {
    val rowRefUpper = rowRef.trim { it <= ' ' }.toUpperCase()
    require(rowRefPattern.matcher(rowRefUpper).find()) { "Invalid format, can not parse row reference [$rowRef].\nFormat must be \"A\", \"J\", \"AC\" etc." }
    var value = 0
    for (x in 0 until rowRefUpper.length) {
        // +1 because the references have no zero A = 1, Z = 26, AA = 27 (no A0!) -> no zeros!
        val charWeight = rowRefUpper[x] - A + 1
        value = value * 26 + charWeight
        require(value >= 0) {
            // If value 0 or negative, integer overflow has occurred, we can't parse a reference that large
            "Value [$rowRefUpper] results in too large value for row index (more than $INTEGER_MAX_VALUE)"
        }
    }
    return value - 1 // Make it zero-base
}

fun indexToRowRef(rowIndex: Int): String {
    if (rowIndex < 0) {
        throw IllegalArgumentException("Negative row index [$rowIndex] is not allowed")
    }
    val sb = StringBuilder()
    var num = rowIndex
    while (num >= 0) {
        val asciiNum = num % 26 + A.toInt()
        sb.append(asciiNum.toChar())
        // -1 because the references have no zero A = 1, Z = 26, AA = 27 (no A0!) -> no zeros!
        num = num / 26 - 1
    }
    return sb.reverse().toString()
}

fun colRefToIndex(colRef: String): Int {
    val colRefUpper = colRef.trim { it <= ' ' }.toUpperCase()
    if (!colRefPattern.matcher(colRefUpper).find()) {
        throw IllegalArgumentException("""Invalid format, can not parse column reference [$colRef].\nFormat must be "1", "8", "11" etc.""")
    }
    // Check that we do not exceed maxint, to avoid integer overflow
    val colRefBigInt = BigInteger(colRefUpper)
    if (colRefBigInt.compareTo(INTEGER_MAX_VALUE) == 1) {
        throw IllegalArgumentException("Value [$colRefUpper] too large for column index; maximum value is $INTEGER_MAX_VALUE")
    }
    return colRefUpper.toInt() - 1 // -1 to make it zero-based
}

fun indexToColRef(colIndex: Int): String {
    if (colIndex < 0) {
        throw IllegalArgumentException("Negative column index [$colIndex] is not allowed")
    }
    return Integer.valueOf(colIndex + 1).toString() // +1 to make it one-based
}
