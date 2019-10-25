package jdkUtils.logcat

import kotlin.reflect.KClass

/**
 * @author ExplodingDragon
 * @version 1.0
 */
interface LogFactory {
    fun outputLog(millisecond: Long,level:Int,clazz: KClass<*>,message: String, exception: Throwable?)
    var isDebug:Boolean
}