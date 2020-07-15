package nl.jhvh.sudoku.base

/** Interface to define constants etc. with regard to [Grid] dimensions.   */
interface Size

/**
 * Minimal allowed base dimension of a [Block] = [MIN_BASE_DIM].
 *  * 1 dimension only: we only support square Sudoku's (blocks of 2x2, 3x3, 4x4 etc.); so no support for, say, 2x3 block.
 */
const val MIN_BASE_DIM: Int = 2

/**
 * Hypothetical maximal allowed base dimension of a [Block] = [MAX_BASE_DIM].
 *  * This is the maximum number that, when squared, does not cause overflow of [Integer.MAX_VALUE] (2_147_483_647).
 *    Does not have much practical meaning, though...
 */
const val MAX_BASE_DIM: Int = 46340

/**
 * Default base dimension of a [Block] = [DEFAULT_BASE_DIM].
 *  * 1 dimension only: we only support square Sudoku's (3x3 etc.); so no support for, say, 2x3 block.
 */
const val DEFAULT_BASE_DIM: Int = 3
