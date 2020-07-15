package nl.jhvh.sudoku.base

/** @return An immutable [List]<[Int]> with [size] elements, starting with 0 and every next element incremented by 1 (so last element is [size]-1)*/
fun incrementFromZero(size: Int): List<Int> = IntArray(size) {it+1}.map { it-1 }
