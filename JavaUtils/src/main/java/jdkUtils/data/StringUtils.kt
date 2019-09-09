package jdkUtils.data

import jdkUtils.io.IOUtils
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.nio.charset.Charset


object StringUtils {

    /**
     * # 读取 InputStream 下所有数据并转换成字符串
     *
     *  读取 InputStream 下所有数据并转换成字符串，并且自动关闭 InputStream
     *
     * @param inputStream InputStream 源IO
     * @param charset Charset 编码
     * @return String 转换的字符串
     */
    @JvmStatic
    fun readInputStream(inputStream: InputStream, charset: Charset = Charsets.UTF_8): String {
        val stringBuilder = StringBuilder()
        try {
            val bytes = inputStream.readBytes()
            stringBuilder.append(bytes.toString(charset))
        } catch (e: Exception) {

        } finally {
            inputStream.close()
        }
        return stringBuilder.toString()
    }


    @JvmStatic
    fun throwableFormat(throws: Throwable): String {
        val outputStream = ByteArrayOutputStream()
        val printWriter = PrintWriter(OutputStreamWriter(outputStream, Charsets.UTF_8), true)
        throws.printStackTrace(printWriter)
        printWriter.flush()
        val array = outputStream.toByteArray()
        printWriter.close()
        outputStream.close()
        return array.toString(Charsets.UTF_8).trim()
    }

    /**
     *  #得到字符串的哈希值
     *
     * @param str String 源字符串
     * @param method String 计算的哈希值类型
     * @return String 哈希值
     */
    @JvmStatic
    fun stringHashHex(str: String, method: String) = IOUtils.calculatedHash(str.byteInputStream(Charsets.UTF_8), method)


    @JvmStatic
    fun stringMd5(str: String) = stringHashHex(str, "md5")

    @JvmStatic
    fun stringSha1(str: String) = stringHashHex(str, "sha1")

    /**
     *  #得到字符串的CRC32校验值
     *
     * @param str String 源字符串
     * @param method String 计算的哈希值类型
     * @return CRC32校验值
     */
    @JvmStatic
    fun stringCrc32Hex(str: String) = IOUtils.calculatedCrc32(str.byteInputStream(Charsets.UTF_8))

}
