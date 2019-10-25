package jdkUtils

import jdkUtils.logcat.DefaultLog
import jdkUtils.logcat.LogFactory

/**
 * @author Explo
 */
object ModConfig{

    /**
     * 默认的打印日志输出模块，控制整个库的日志打印
     */
    @Volatile
    @JvmStatic
    var logFactory :LogFactory =  DefaultLog()

    /**
     * 开启 Debug模式，会输出大量的运行关键信息
     */
    @JvmStatic
    val debug
        get() = logFactory.isDebug



}
