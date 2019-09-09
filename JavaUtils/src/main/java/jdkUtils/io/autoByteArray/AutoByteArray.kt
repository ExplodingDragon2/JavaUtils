package jdkUtils.io.autoByteArray

import java.io.Closeable
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset
import kotlin.random.Random

/**
 * @author ExplodingDragon
 * @version 1.0
 */
abstract class AutoByteArray : Closeable {

    /**
     * 自动数组的大小
     */
     abstract val size: Long

    /**
     * # 得到索引 l 的Byte数据
     * @param l Long 索引
     * @return Byte 数据信息
     * @throws IndexOutOfBoundsException 如果传入索引存在问题
     * @throws IOException 如果处理过程中发生IO错误
     */
    @Throws(IndexOutOfBoundsException::class,IOException::class)
    abstract operator fun get(l: Long): Byte

    /**
     * # 以此得到一个全新的InputStream
     *
     * 注意，返回的InputStream 必定与此绑定，此关闭，InputStream 也必须同时关闭
     *
     * @return InputStream 以此为蓝本的新InputStream
     * @throws IOException 如果处理过程中发生IO错误
     */

    @Throws(IOException::class)
    abstract fun openInputStream(): InputStream


    /**
     *  # 得到字符串
     *
     * @param offset Long 偏移位置
     * @param length Int 数据长度
     * @param charset Charset 编码
     * @return String 计算的字符串
     */
    fun toString(offset: Long = 0, length: Int = size.toInt(), charset: Charset = Charsets.UTF_8): String {
        return String(toByteArray(offset, length), charset)
    }


    /**
     * # 得到指定长度ByteArray
     * @param offset Long  偏移量
     * @param length Int 长度
     * @return ByteArray
     */
    fun toByteArray(offset: Long, length: Int): ByteArray {
        if (offset < 0 || length < 0 || length > size - offset){
            throw IndexOutOfBoundsException()
        }
        if (length == 0) {
            return ByteArray(0)
        }
        val inputStream = openInputStream()
        inputStream.skip(offset)
        val array = ByteArray(length)
        inputStream.read(array)
        return array
    }


    val search: AutoByteArraySearch by lazy {
        AutoByteArraySearch(this)
    }



    /**
     * # 复制一个新的 AutoByteArray
     *
     * @param tempFile File 临时文件位置
     * @param offset Long 数据偏移
     * @param len Long 复制长度
     * @return AutoByteArray 的 AutoByteArray
     */
    fun copyOf(tempFile: File = AutoByteArrayOutputStream.getTempFile(Random.nextDouble()), offset: Long = 0, len: Long = size): AutoByteArray {
        if (offset < 0 || len < 0 || len > size - offset){
            throw IndexOutOfBoundsException()
        }
        if (len == 0L){
            return ArrayAutoByteArray(byteArrayOf(get(offset)))
        }
        val outputStream = AutoByteArrayOutputStream(tempFile)
        val inputStream = openInputStream()
        inputStream.skip(offset)
        val array = ByteArray(1024)
        var length = len
        while (true){
            inputStream.read(array)
            if (length >= 1024){
                length-=1024
                outputStream.write(array)
            }else{
                outputStream.write(array,0,length.toInt())
                break
            }
        }
        return outputStream.autoByteArray
    }

}