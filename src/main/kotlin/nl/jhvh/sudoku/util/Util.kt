package nl.jhvh.sudoku.util

import java.util.Collections.unmodifiableList

/**
 * @return An immutable [List]<[Int]> with [size] elements, starting with 0 and every next element incremented by 1 (so last element is [size]-1)
 * @throws IllegalArgumentException if [size] is negative
 */
@Throws(IllegalArgumentException::class)
fun incrementFromZero(size: Int): List<Int> {
    require(size >= 0, { "Negative size $size not allowed" })
    // IntRange(...).toList() returns a MutableList, except when the result is empty...
    // For consistent behaviour, let's make it immutable
    return unmodifiableList((0..size - 1).toList())
}

