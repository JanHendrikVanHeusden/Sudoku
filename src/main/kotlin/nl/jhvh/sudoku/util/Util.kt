package nl.jhvh.sudoku.util

/** @return An immutable [List]<[Int]> with [size] elements, starting with 0 and every next element incremented by 1 (so last element is [size]-1)*/
fun incrementFromZero(size: Int): List<Int> {
    require(size >= 0, { "Negative size $size not allowed" })
    return IntRange(start = 0, endInclusive = size - 1).toList()
}

fun intRangeSet(minVal: Int, maxVal: Int): Set<Int> = IntRange(start = minVal, endInclusive = maxVal).toSet()
