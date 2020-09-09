package nl.jhvh.sudoku.util

import java.util.Collections.unmodifiableList
import java.util.Collections.unmodifiableSet

/**
 * @return An immutable [List]<[Int]> with [size] elements, starting with 0 and every next element incremented by 1 (so last element is [size]-1)
 * @throws IllegalArgumentException if [size] is negative
 */
@Throws(IllegalArgumentException::class)
fun incrementFromZero(size: Int): List<Int> {
    require(size >= 0, { "Negative size $size not allowed" })
    // IntRange(...).toList() returns a MutableList, except when the result is empty...
    // For consistent behaviour, let's make it immutable
    return unmodifiableList(IntRange(start = 0, endInclusive = size - 1).toList())
}

/**
 * Return an immutable [Set]<[Int]> starting with [minVal] and ending with [maxVal]; every next element incremented by 1
 * @return the [Set] as requested; or an empty [Set] if [maxVal] < [minVal]
 */
fun intRangeSet(minVal: Int, maxVal: Int): Set<Int>
// IntRange(...).toSet() returns a MutableSet, except when the result is empty...
// For consistent behaviour, let's make it immutable
        = unmodifiableSet(IntRange(start = minVal, endInclusive = maxVal).toSet())
