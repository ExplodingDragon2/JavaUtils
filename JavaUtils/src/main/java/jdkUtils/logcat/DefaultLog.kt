package jdkUtils.logcat

import java.text.SimpleDateFormat
import java.util.*
import kotlin.reflect.KClass

class DefaultLog : LogFactory {
        private val format = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())

        override fun outputLog(millisecond: Long, level: Int, clazz: KClass<*>, message: String, exception: Throwable?) {
            val output = StringBuilder()
            output.append("${format.format(millisecond)} - ${String.format("%-5s", Log.logIdName(level))} - ${clazz.java.name} : $message")
            exception?.let {
                output.append("\r\n")
                output.append(Log.exceptionToString(exception))
            }
            if (level >= Log.LOGGER_WARN){
                System.err.println(output)
            }else{
                println(output)
            }
        }
        override var isDebug: Boolean = true
    }