package nl.jhvh.sudoku.base

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

/** Minimal relevant base dimension of a [Block] = 2. */
const val MIN_BLOCK_SIZE: Int = 2

/**
 * Arbitrary maximal allowed base dimension of a [Block] = 100.
 *  * This is used only to avoid too large Sudokus being constructed that would do nothing but clog memory and CPU.
 *  * The value may be too high yet, depending on JVM settings etc.
 *  * The theoretical maximum would be 46340: this is the maximum number that, when squared, does not cause overflow
 *    of [Integer.MAX_VALUE] (2_147_483_647). Does not have much practical meaning, though...
 */
const val MAX_BLOCK_SIZE: Int = 100

/**
 * Default base dimension of a [Block] = [DEFAULT_BLOCK_SIZE].
 *  * 1 dimension only: we only support square Sudoku's (3x3 etc.); so no support for, say, 2x3 block.
 */
const val DEFAULT_BLOCK_SIZE: Int = 3

/** Fixed minimum value to be entered in a cell  */
const val CELL_MIN_VALUE: Int = 1

fun gridSize(blockSize: Int): Int = blockSize * blockSize

fun maxValue(blockSize: Int): Int = blockSize * blockSize

fun validateBlockSize(blockSize: Int) {
    require(blockSize >= MIN_BLOCK_SIZE) { "Given blocksize is $blockSize but must at least $MIN_BLOCK_SIZE" }
    require(blockSize <= MAX_BLOCK_SIZE) { "Given blocksize is $blockSize but must be at most $MAX_BLOCK_SIZE" }
}