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

import mu.KLogger
import mu.NamedKLogging
import org.apache.logging.slf4j.Log4jLogger
import org.slf4j.ILoggerFactory
import org.slf4j.LoggerFactory
import org.slf4j.event.Level

internal inline fun <reified T : Any> T.log(name: String = ""): KLogger = lazy { NamedKLogging(if (name.isBlank()) T::class.java.name else name).logger }.value

private val loggerFactory: ILoggerFactory = LoggerFactory.getILoggerFactory()

// Not very elegant - uses the fact that the underlying slf4j implementation is Log4J 2
// Used to provide a logger implementation that you dynamically can pass a level to
private fun log4J2Logger(name: String): Log4jLogger = loggerFactory.getLogger(name) as Log4jLogger

/**
 * If (and only if) the [condition] is `false`:
 *  * the [message] is logged (insofar enabled);
 *  * an [IllegalArgumentException] is thrown with the given [message]
 * @param condition The condition to check
 * @param logLevel The [Level] to log with, if enabled; default = [Level.WARN]
 * @param message The `() -> String` message provider that will be evaluated only when needed
 */
@Throws(IllegalArgumentException::class)
internal inline fun <reified T : Any> T.requireAndLog(condition: Boolean, logLevel: Level = Level.WARN, message: () -> String) {
    if (!condition) {
        val logger= log4J2Logger(T::class.java.name)
        logger.log(null, logger.name, logLevel.toInt(), message.invoke(), null, null)
        require(condition, message)
    }
}

/**
 * If (and only if) the [condition] is `false`:
 *  * the [message] is logged (insofar enabled);
 *  * an [IllegalStateException] is thrown with the given [message]
 * @param condition The condition to check
 * @param logLevel The [Level] to log with, if enabled; default = [Level.ERROR]
 * @param message The `() -> String` message provider that will be evaluated only when needed
 */
@Throws(IllegalStateException::class)
internal inline fun <reified T : Any> T.checkAndLog(condition: Boolean, logLevel: Level = Level.ERROR, message: () -> String) {
    if (!condition) {
        val logger= log4J2Logger(T::class.java.name)
        logger.log(null, logger.name, logLevel.toInt(), message.invoke(), null, null)
        check(condition, message)
    }
}
