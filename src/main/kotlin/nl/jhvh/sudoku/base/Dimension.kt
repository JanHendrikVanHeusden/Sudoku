package nl.jhvh.sudoku.base

/**
 * Minimal allowed base dimension of a [Block] = [MIN_BLOCK_SIZE].
 *  * 1 dimension only: we only support square Sudoku's (blocks of 2x2, 3x3, 4x4 etc.); so no support for, say, 2x3 block.
 */
const val MIN_BLOCK_SIZE: Int = 2

/**
 * Hypothetical maximal allowed base dimension of a [Block] = [MAX_BLOCK_SIZE].
 *  * This is the maximum number that, when squared, does not cause overflow of [Integer.MAX_VALUE] (2_147_483_647).
 *    Does not have much practical meaning, though...
 */
const val MAX_BLOCK_SIZE: Int = 46340

/**
 * Default base dimension of a [Block] = [DEFAULT_BLOCK_SIZE].
 *  * 1 dimension only: we only support square Sudoku's (3x3 etc.); so no support for, say, 2x3 block.
 */
const val DEFAULT_BLOCK_SIZE: Int = 3

/** Fixed minimum value to be entered in a cell  */
const val CELL_MIN_VALUE: Int = 1

fun gridSize(blockSize: Int): Int {
    validateBlockSize(blockSize)
    return blockSize * blockSize
}

fun maxValue(blockSize: Int): Int {
    validateBlockSize(blockSize)
    return blockSize * blockSize
}

fun validateBlockSize(blockSize: Int) {
    require(blockSize > 0) { "Given blocksize is $blockSize but must be positive ( > 0 )" }
    require(blockSize <= MAX_BLOCK_SIZE) { "Given blocksize is $blockSize but must be at most $MAX_BLOCK_SIZE" }
}