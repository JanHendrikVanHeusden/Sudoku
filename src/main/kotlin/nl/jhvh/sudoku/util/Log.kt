package nl.jhvh.sudoku.util

import mu.KLogger
import mu.NamedKLogging
import org.apache.logging.slf4j.Log4jLogger
import org.slf4j.ILoggerFactory
import org.slf4j.LoggerFactory
import org.slf4j.event.Level

inline fun <reified T : Any> T.log(name: String = ""): KLogger = lazy { NamedKLogging(if (name.isBlank()) T::class.java.name else name).logger }.value

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
fun Any.requireAndLog(condition: Boolean, logLevel: Level = Level.WARN, message: () -> String) {
    if (!condition) {
        val logger= log4J2Logger(this.javaClass.name)
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
fun Any.checkAndLog(condition: Boolean, logLevel: Level = Level.ERROR, message: () -> String) {
    if (!condition) {
        val logger= log4J2Logger(this.javaClass.name)
        logger.log(null, logger.name, logLevel.toInt(), message.invoke(), null, null)
        check(condition, message)
    }
}
