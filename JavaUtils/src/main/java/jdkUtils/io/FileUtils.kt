package jdkUtils.io

import jdkUtils.data.StringUtils
import jdkUtils.logcat.Logger
import java.io.*
import java.nio.charset.Charset
import java.util.*
import kotlin.math.pow

object FileUtils {
    private val logger = Logger.getLogger(this)

    val hexDigits = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')



    @Throws(IOException::class)
    private fun fileChooseException(file: File) {
        if (file.isFile.not()) {
            throw FileNotFoundException("[${file.absolutePath}] exists.")
        }
        if (file.canRead().not()) {
            throw IOException("[${file.absolutePath}] can't read.")
        }
    }

    @Throws(IOException::class)
    private fun fileChoose(file: File): Boolean {
        if (file.isFile.not()) {
            logger.debug("[${file.absolutePath}] exists.")
            return false
        }
        if (file.canRead().not()) {
            logger.debug("[${file.absolutePath}] can't read.")
            return false
        }
        return true

    }

    /**
     *
     *
     * 以特定编码读取文件中所有字符
     *
     *
     * @param file     文件地址
     * @param charset 编码
     * @return 返回文件所有 **特定编码** 后的字符串，
     */
    @JvmStatic
    fun readFile(file: File, charset: Charset = Charsets.UTF_8): String {
        fileChooseException(file)
        return StringUtils.readInputStream(FileInputStream(file), charset)
    }

    /**
     *
     * 将数据写入到文件
     *
     * @param file 输出的位置
     * @param data 数据源
     * @param charset 编码方式
     * @return 是否成功
     */
    @JvmStatic
    fun writeFile(file: File, data: String, charset: Charset = Charsets.UTF_8): Boolean {
        if (fileChoose(file)) {
            return false
        }
        var result: Boolean
        val outputStream = FileOutputStream(file)
        val outputStreamWriter = OutputStreamWriter(outputStream, charset)
        try {
            outputStreamWriter.write(data)
            outputStreamWriter.flush()
            outputStream.flush()
            result = true
        } catch (e: Exception) {
            logger.debug("在传送时发生错误！\n" + StringUtils.throwableFormat(e))
            result = false
        } finally {
            try {
                outputStreamWriter.close()
            } catch (e: Exception) {
                logger.debug("在关闭时发生错误！\n" + StringUtils.throwableFormat(e))
            }

        }
        return result
    }


    /**
     *
     * 递归删除文件(夹)
     * @param file 指定的文件夹 或者 文件
     * @return 是否删除成功
     */
    @JvmStatic
    fun delete(file: File): Boolean {
        return if (file.isFile) {
            file.delete()
        } else {
            try {
                if (!file.delete()) {
                    for (childrenFile in file.listFiles()!!) {
                        if (!delete(childrenFile)) {
                            logger.debug("删除文件[${childrenFile.absolutePath}] 失败了 .")
                            return false
                        }
                    }
                    delete(file)
                } else {
                    true
                }
            } catch (e: Exception) {
                logger.debug("在删除文件[${file.absolutePath}]时发生错误！\n" + StringUtils.throwableFormat(e))
                false
            }
        }
    }


    /**
     *
     * 将文件大小转换为可视化文本
     *
     * @param length 文件大小（单位：B）
     * @param i 小数点位数
     * @return 格式化的字符串
     */
    @JvmStatic
    fun lengthFormat(length: Long, i: Int = 2): String {
        val maxByte = doubleArrayOf(1.0, 2.0.pow(10.0), 2.0.pow(20.0), 2.0.pow(30.0), 2.0.pow(40.0), 2.0.pow(50.0), 2.0.pow(60.0), 2.0.pow(70.0), 2.0.pow(80.0), 2.0.pow(90.0), 2.0.pow(100.0), 2.0.pow(110.0), 2.0.pow(120.0))
        val byteUnits = arrayOf("B", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB", "BB", "NB", "DB", "CB")
        var unitPosition = 0
        for (position in maxByte.indices) {
            if (length < maxByte[position]) {
                break
            }
            unitPosition = position
        }
        val decimal = 10.0.pow(i.toDouble())
        val postConversionData = (length / maxByte[unitPosition] * decimal).toLong()
        val endSize = postConversionData / decimal
        return String.format("%.0${i}f", endSize) + byteUnits[unitPosition]
    }

    /**
     *
     * 对File 的 List进行排序
     * @param fileList 原始数据
     */
    @JvmStatic
    fun fileSort(fileList: List<File>) {
        Collections.sort(fileList) { o1, o2 ->
            if (o1.isDirectory && o2.isFile) {
                return@sort -1
            } else if (o1.isFile && o2.isDirectory) {
                return@sort 1
            } else {
                val name1 = o1.name
                val name2 = o2.name
                if (name1 == name2)
                    return@sort 0
                val char1 = name1.toCharArray()
                val char2 = name2.toCharArray()
                val len = if (char1.size > char2.size) char2.size else char1.size
                if (Character.isUpperCase(char1[0]) && !Character.isUpperCase(char2[0])) {
                    return@sort -1
                } else if (!Character.isUpperCase(char1[0]) && Character.isUpperCase(char2[0])) {
                    return@sort 1
                }
                for (i in 0 until len) {
                    val c1 = getCharIndex(char1[i])
                    val c2 = getCharIndex(char2[i])
                    return@sort c1 - c2
                }
                return@sort char1.size - char2.size
            }
        }
    }

    /**
     *
     * 计算单一char 字符的GBK位置
     * @param c 字符
     * @return 顺序
     */
    @JvmStatic
    private fun getCharIndex(c: Char): Int {
        val bytes = StringBuffer().append(c).toString().toByteArray(charset("gbk"))

        if (bytes.size == 1) {
            return bytes[0].toInt()
        }
        val a = bytes[0] - 0xA0 + 256
        val b = bytes[1] - 0xA0 + 256

        return a * 100 + b
    }


    /**
     *  得到文件的哈希值
     *
     * @param file File 源文件位置
     * @param method String 计算的哈希值类型
     * @return String 哈希值
     */
    @JvmStatic
    fun fileHashHex(file: File, method: String): String {
        fileChooseException(file)
        return IOUtils.calculatedHash(file.inputStream(),method)
    }

    /**
     * # 计算文件MD5哈希值
     * @param file File 需要计算的源文件
     * @return String 校验值
     */
    @JvmStatic
    fun fileMd5Hex(file: File) = fileHashHex(file,"MD5")

    /**
     * # 计算文件SHA1哈希值
     * @param file File 需要计算的源文件
     * @return String 校验值
     */
    @JvmStatic
    fun fileSha1Hex(file: File) = fileHashHex(file,"SHA1")

    /**
     * # 计算文件CRC32 校验值
     * @param file File 需要计算的源文件
     * @return String 校验值
     */
    @JvmStatic
    fun fileCrc32Hex(file: File) = IOUtils.calculatedCrc32(file.inputStream());

}
