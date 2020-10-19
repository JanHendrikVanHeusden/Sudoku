package nl.jhvh.sudoku.util

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

import org.paukov.combinatorics3.Generator
import java.util.Collections.unmodifiableList
import java.util.stream.Stream

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

/**
 * Given a [Collection] of [elements] of type [T], gives all possible combinations with length [combinationLength].
 * * Take care: the number of [List]s in the [Stream] can be huge:
 *   # [elements.size]! / ( ([elements.size] - [combinationLength])! * [combinationLength]! )
 *   # So with 20 elements and [combinationLength] = 10 you get about 185 k results...
 * @param elements The input [Collection]
 * @param combinationLength The desired length of the combinations
 * @return A [Stream] of [List]s of [T].
 *  * The [Stream] will be empty if [combinationLength] > [elements.size]
 *  * The [Stream] will contain a single empty [List] if [combinationLength] == 0
 * @throws IllegalArgumentException when [combinationLength] < 0 and [elements] is not empty
 */
@Throws(IllegalArgumentException::class)
fun <T> allCombinations(elements: Collection<T>, combinationLength: Int): Stream<List<T>> {
    // Negative values would cause ArrayIndexOutOfBoundsException, better let if fail with clear message
    require(combinationLength >= 0 || elements.isEmpty()) { "Desired combinationLength must be 0 or more, but is $combinationLength" }
    return Generator.combination(elements).simple(combinationLength).stream()
}
