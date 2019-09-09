package jdkUtils.logcat

import jdkUtils.ModConfig
import jdkUtils.data.StringUtils
import kotlin.reflect.KClass



/**
 *
 * # 自定义日志工具
 *
 *s
 * 打印所有日志
 *
 *
 * @author ExplodingDragon
 * @version 1.0
 */
class Logger private constructor(private val kClass: KClass<*>) : Log() {
    override fun info(message: String, exception: Throwable?) {
        logCallback(LOGGER_INFO,message,exception)
    }

    override fun debug(message: String, exception: Throwable?) {
        if (debug) {
            logCallback(LOGGER_DEBUG,message,exception)
        }
    }

    override fun warn(message: String, exception: Throwable?) {
        logCallback(LOGGER_WARN,message,exception)
    }

    override fun error(message: String, exception: Throwable?) {
        logCallback(LOGGER_ERROR,message,exception)
    }


   private fun logCallback(level:Int,message: String, exception: Throwable?){
       ModConfig.logFactory.outputLog(System.currentTimeMillis(),level,kClass,message,exception)
   }

    /**
     * # 判断是否为DEBUG 模式
     */
    val debug:Boolean
    get() = ModConfig.logFactory.isDebug

    companion object {
        init {
            printLogo("/res/Logo.txt")
        }

        @JvmStatic
        fun getLogger(clazz: Class<*>): Logger {
            return Logger(clazz.kotlin)
        }
        @JvmStatic
        fun getLogger(clazz: KClass<*>): Logger {
            return Logger(clazz)
        }

        @JvmStatic
        fun printLogo(path: String) {
            try {
                val logo = StringUtils.readInputStream(Logger::class.java.getResourceAsStream(path))
                System.err.println("\n$logo\n")
            } catch (e: Exception) {
                System.err.println("Logo 打印失败！")
            }

        }
        @JvmStatic
        fun getLogger(any: Any) = getLogger(any::class)
    }
}
