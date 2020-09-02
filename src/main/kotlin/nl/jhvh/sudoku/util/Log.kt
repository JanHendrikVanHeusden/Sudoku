package nl.jhvh.sudoku.util

import mu.KLogger
import mu.NamedKLogging

fun Any.log(name: String = ""): KLogger = lazy { NamedKLogging(if (name.isBlank()) this.javaClass.name else name).logger }.value
