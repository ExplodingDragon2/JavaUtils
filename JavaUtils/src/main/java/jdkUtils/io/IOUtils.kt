package jdkUtils.io

import java.io.IOException
import java.io.InputStream
import java.math.BigInteger
import java.security.MessageDigest
import java.util.zip.CRC32
import java.util.zip.CheckedInputStream


object IOUtils {
    /**
     * #计算一个流的哈希信息
     *
     * @param input InputStream 标准流
     * @param method String 哈希类型
     * @return String 哈希值
     * @throws IOException 如果发生任意错误都会抛出
     */
    @JvmStatic
    @Throws(IOException::class, Exception::class, RuntimeException::class)
    fun calculatedHash(input: InputStream, method: String): String {
        var result = ""
        var resultError: Exception? = null
        try {
            val md = MessageDigest.getInstance(method) 
            val dataBytes = ByteArray(4096)
            var read: Int
            while (true) {
                read = input.read(dataBytes)
                if (read == -1) {
                    break
                }
                md.update(dataBytes, 0, read)
            }
            val bytes = md.digest()
            result = BigInteger(1, bytes).toString(16).toUpperCase()
        } catch (e: Exception) {
            resultError = e
        } finally {
            try {
                input.close()
            } catch (e: Exception) {

            }
            if (result.isNotEmpty()) {
                return result
            } else {
                resultError ?: throw NullPointerException("未知错误！！！")
                resultError.let {
                    throw it
                }
            }
        }
    }

    /**
     * # 计算流的CRC32 值
     *
     * @param inputStream InputStream 原始流
     * @return String CRC32 校验值
     * @throws Exception 如果发生错误
     */
    @JvmStatic
    @Throws(Exception::class)
    fun calculatedCrc32(inputStream: InputStream): String {
        val crc32 = CRC32()
        var result = ""
        var resultError: Exception? = null
        try {
            val stream = CheckedInputStream(inputStream, crc32)
            while (true) {
                if (stream.read() != -1){
                    break
                }
            }
            stream.close()
            inputStream.close()
            result = crc32.value.toString(16).toUpperCase()
        } catch (e: Exception) {
            resultError = e
        } finally {
            try {
                inputStream.close()
            } catch (e: Exception) {

            }
            if (result.isNotEmpty()) {
                return result
            } else {
                resultError ?: throw NullPointerException("未知错误！！！")
                resultError.let {
                    throw it
                }
            }
        }

    }
}