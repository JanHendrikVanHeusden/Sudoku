package nl.jhvh.sudoku.util

import mu.KLogger
import mu.NamedKLogging
import org.apache.logging.slf4j.Log4jLogger
import org.slf4j.ILoggerFactory
import org.slf4j.LoggerFactory
import org.slf4j.event.Level

inline fun <reified T : Any> T.log(name: String = ""): KLogger = lazy { NamedKLogging(if (name.isBlank()) T::class.java.name else name).logger }.value

private val loggerFactory: ILoggerFactory = LoggerFactory.getILoggerFactory()

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
        // Not very elegant - uses the fact that the underlying slf4j implementation is Log4J 2
        // to provide a logger implementation that you dynamically can pass a level to.
        val log4J2Logger: Log4jLogger = loggerFactory.getLogger(this.javaClass.name) as Log4jLogger
        log4J2Logger.log(null, log4J2Logger.name, logLevel.toInt(), message.invoke(), null, null)
        require(condition, message)
    }
}
