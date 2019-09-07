package jdkUtils.io

import jdkUtils.ModConfig
import jdkUtils.data.StringUtils
import java.io.*
import java.lang.Math.pow
import java.nio.charset.Charset
import java.util.*
import kotlin.math.pow

object FileUtils {
    val hexDigits = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')

    @Throws(IOException::class)
    private fun fileChooseException(file: File){
        if (file.isFile.not()) {
            throw FileNotFoundException("[${file.absolutePath}] exists.")
        }
        if (file.canRead().not()){
            throw IOException("[${file.absolutePath}] can't read.")
        }
    }

    @Throws(IOException::class)
    private fun fileChoose(file: File):Boolean{
        if (file.isFile.not()) {
            ModConfig.printDebug ("[${file.absolutePath}] exists.")
            return false
        }
        if (file.canRead().not()){
            ModConfig.printDebug("[${file.absolutePath}] can't read.")
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
    fun fileToString(file: File, charset: Charset = Charsets.UTF_8): String {
        fileChooseException(file)
        return StringUtils.readInputStream(FileInputStream(file), charset)
    }

    /**
     *
     * 将数据写入到文件
     *
     * @param file 输出的位置
     * @param data 数据源
     * @param coding 编码方式
     * @return 是否成功
     */
    @JvmStatic
    fun write(file: File, data: String,  charset: Charset = Charsets.UTF_8): Boolean {
        if (fileChoose(file)){
            return false
        }
        var result: Boolean
        val outputStream  = FileOutputStream(file)
        val outputStreamWriter = OutputStreamWriter(outputStream, charset)
        try {
            outputStreamWriter.write(data)
            outputStreamWriter.flush()
            outputStream.flush()
            result = true
        } catch (e: Exception) {
            ModConfig.printDebug("在传送时发生错误！\n"+ StringUtils.throwableFormat(e))
            result = false
        } finally {
            try {
                outputStreamWriter.close()
            } catch (e: Exception) {
                ModConfig.printDebug("在关闭时发生错误！\n"+ StringUtils.throwableFormat(e))
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
                            ModConfig.printDebug("删除文件[${childrenFile.absolutePath}] 失败了 .")
                            return false
                        }
                    }
                    delete(file)
                } else {
                    true
                }
            } catch (e: Exception) {
                ModConfig.printDebug("在删除文件[${file.absolutePath}]时发生错误！\n"+ StringUtils.throwableFormat(e))
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
        val maxByte = doubleArrayOf(1.0, 2.0.pow(10.0), 2.0.pow(20.0), 2.0.pow(30.0), pow(2.0, 40.0), 2.0.pow(50.0), pow(2.0, 60.0), pow(2.0, 70.0), 2.0.pow(80.0), 2.0.pow(90.0), 2.0.pow(100.0), pow(2.0, 110.0), 2.0.pow(120.0))
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
        return String.format("%.0${i}f",endSize) + byteUnits[unitPosition]
    }

    /**
     *
     * 对File 的 List进行排序
     * @param fileList 原始数据
     */
    fun sort(fileList: List<File>) {
        Collections.sort(fileList) { o1, o2 ->
            if (o1.isDirectory && o2.isFile) {
                return@sort - 1
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
                    return@sort - 1
                } else if (!Character.isUpperCase(char1[0]) && Character.isUpperCase(char2[0])) {
                    return@sort 1
                }
                for (i in 0 until len) {
                    val c1 = getCharIndex(char1[i])
                    val c2 = getCharIndex(char2[i])
                    return@sort c1 -c2
                }
                return@sort char1 . size -char2.size
            }
        }
    }

    /**
     *
     * 计算单一char 字符的GBK位置
     * @param c 字符
     * @return 顺序
     */
    fun getCharIndex(c: Char): Int {
        var bytes: ByteArray? = null
        try {
            bytes = StringBuffer().append(c).toString().toByteArray(charset("gbk"))
        } catch (e: UnsupportedEncodingException) {
        }

        if (bytes!!.size == 1) {
            return bytes[0].toInt()
        }
        val a = bytes[0] - 0xA0 + 256
        val b = bytes[1] - 0xA0 + 256

        return a * 100 + b
    }



}
