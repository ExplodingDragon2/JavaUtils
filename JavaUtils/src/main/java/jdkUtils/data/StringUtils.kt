package jdkUtils.data

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
    fun readInputStream(inputStream: InputStream,charset: Charset = Charsets.UTF_8): String {
        val stringBuilder = StringBuilder()
        try {
            val bytes = inputStream.readBytes()
            stringBuilder.append(bytes.toString(charset))
        }catch (e:Exception){

        }finally {
            inputStream.close()
        }
        return stringBuilder.toString()
    }

    @JvmStatic
    fun throwableFormat(throws:Throwable):String{
        val outputStream = ByteArrayOutputStream()
        val printWriter = PrintWriter(OutputStreamWriter(outputStream,Charsets.UTF_8),true)
        throws.printStackTrace(printWriter)
        printWriter.flush()
        val array = outputStream.toByteArray()
        printWriter.close()
        outputStream.close()
        return array.toString(Charsets.UTF_8).trim()
    }


}
