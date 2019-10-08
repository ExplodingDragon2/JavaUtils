package jdkUtils

import jdkUtils.logcat.Log
import jdkUtils.logcat.LogFactory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.reflect.KClass

/**
 * @author Explo
 */
object ModConfig{

    /**
     * 默认的打印日志输出模块，控制整个库的日志打印
     */
    @Volatile
    @JvmStatic
    var logFactory:LogFactory =  DefaultLog()

    /**
     * 开启 Debug模式，会输出大量的运行关键信息
     */
    @Volatile
    @JvmStatic
    var debug = false

    /**
     * 是否为调试模式
     */
    @JvmStatic
    val isDebug: Boolean
        get() = debug


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
        override val isDebug: Boolean
            get() = debug
    }
}
