package nl.jhvh.sudoku.util

import mu.KLogger
import mu.NamedKLogging

fun Any.log(name: String = ""): KLogger = lazy { NamedKLogging(if (name.isBlank()) this.javaClass.typeName else name).logger }.value

fun log(clazz: Class<*>): KLogger = lazy { NamedKLogging(clazz.typeName).logger }.value

fun log(obj: Any): KLogger = log(obj.javaClass)
