package jdkUtils.logcat

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.OutputStreamWriter
import java.io.PrintWriter

//@Deprecated("可以使用新的Logcat方案")
abstract class Log {


    abstract fun info(message: String, exception: Throwable? = null)

    final fun info(message: String) {
        info(message, null)
    }

    final fun info(message: String, exception: Throwable? = null, vararg args: Any) {
        info(String.format(message, args), exception)
    }

    abstract fun debug(message: String, exception: Throwable? = null)

    final fun debug(message: String) {
        debug(message, null)
    }

    final fun debug(message: String, exception: Throwable? = null, vararg args: Any) {
        debug(String.format(message, args), exception)
    }

    abstract fun warn(message: String, exception: Throwable? = null)

    final fun warn(message: String) {
        warn(message, null)
    }

    final fun warn(message: String, exception: Throwable? = null, vararg args: Any) {
        warn(String.format(message, args), exception)
    }

    abstract fun error(message: String, exception: Throwable? = null)

    final fun error(message: String) {
        error(message, null)
    }

    final fun error(message: String, exception: Throwable? = null, vararg args: Any) {
        error(String.format(message, args), exception)
    }

    companion object {
        /**
         *  LogCat 日志下调试代号
         */
        const val LOGGER_DEBUG = 0
        /**
         *  LogCat 日志下普通输出代号
         */
        const val LOGGER_INFO = 1
        /**
         *  LogCat 日志下警告输出代号
         */
        const val LOGGER_WARN = 2
        /**
         *  LogCat 日志下错误代号
         */
        const val LOGGER_ERROR = 3

        @JvmStatic
        fun logIdName(id: Int): String {
            return when (id) {
                LOGGER_DEBUG -> "DEBUG"
                LOGGER_INFO -> "INFO"
                LOGGER_WARN -> "WARN"
                LOGGER_ERROR -> "ERROR"
                else -> "UNKNOWN"
            }
        }
        @JvmStatic
        fun exceptionToString(exception: Throwable): String {
            val outputStream = ByteArrayOutputStream()
            val printWriter = PrintWriter(OutputStreamWriter(outputStream, Charsets.UTF_8),true)
            exception.printStackTrace(printWriter)
            printWriter.flush()
            printWriter.close()
            try {
                outputStream.close()
            } catch (e: IOException) {
            }
            return String(outputStream.toByteArray()).trim()
        }
    }
}