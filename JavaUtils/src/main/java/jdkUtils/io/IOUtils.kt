package jdkUtils.io

import java.io.InputStream
import java.math.BigInteger
import java.security.MessageDigest



object IOUtils {
    /**
     * #计算一个流的哈希信息
     *
     * @param input InputStream 标准流
     * @param method String 哈希类型
     * @return String 哈希值
     * @throws Exception 如果发生任意错误都会抛出
     */
    @JvmStatic
    @Throws( Exception::class)
    fun calculatedHash(input: InputStream, method: String): String {
        val md5 = StringBuffer()
        val md = MessageDigest.getInstance(method) ?: throw UnknownError("未知类型的方式")
        val dataBytes = ByteArray(4096)
        try {
            var read = 0
            while (true) {
                read = input.read(dataBytes)
                if (read == -1) {
                    break
                }
                md.update(dataBytes, 0, read)
            }
            val bytes = md.digest() ?: throw RuntimeException("计算哈希值时发生问题！")
            return BigInteger(1, bytes).toString(16).toUpperCase()
        } catch (e: Exception) {
            try {
                input.close()
            } catch (e: Exception) {

            }
            throw RuntimeException("无法得到哈希值[$method].", e)
        }
    }
}