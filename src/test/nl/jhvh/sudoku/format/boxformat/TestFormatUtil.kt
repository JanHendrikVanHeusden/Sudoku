package nl.jhvh.sudoku.format.boxformat

import nl.jhvh.sudoku.format.lineSeparator

fun String.tidy() = this.trimIndent().replace(Regex("""([\r\n])+"""), lineSeparator)
