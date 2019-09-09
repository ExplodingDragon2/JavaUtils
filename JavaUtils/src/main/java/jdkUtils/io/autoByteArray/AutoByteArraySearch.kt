package jdkUtils.io.autoByteArray

import jdkUtils.io.IOUtils
import java.nio.charset.Charset
import java.util.*

/**
 * @author ExplodingDragon
 * @version 1.0
 */
class AutoByteArraySearch(private val autoByteArray: AutoByteArray) {


    /**
     * # 使用Java 实现的算法进行数据搜索
     *
     * 在Byte数组中进行快速搜索，
     * 现已改用 Sunday算法进行搜索，弃用了KMP
     *
     * @param pat ByteArray 搜索源
     * @param index Long 开始位置
     * @return Long 第一次出现的位置
     * @throws IndexOutOfBoundsException 如果传入数据错误
     */
    @Throws(IndexOutOfBoundsException::class)
    fun search(pat: ByteArray, index: Long = 0): Long = sundaySearch(pat, index)

    /**
     *
     * # 数据切割
     *
     * > Test pass.
     *
     *  对数据进行预切割，返回对应的切割数据的位置
     *
     * @param pat ByteArray 切割条件
     * @param index Long 切割起始位置
     * @return LongArray 得到的切割位置
     */
    fun spit(pat: ByteArray, index: Long = 0): LinkedList<Long> {
        if (index < 0 || pat.isEmpty()) {
            throw IndexOutOfBoundsException("index < 0 || pat.isEmpty() => index = $index")
        }

        val patSize = pat.size
        val list = LinkedList<Long>()
        var startIndex = index
        while (true) {
            val search = search(pat, startIndex)
            if (search == -1L) {
                break
            }
            when {
                search == index -> list.add(search + patSize)
                index == startIndex -> {
                    list.add(0)
                    list.add(search - 1)
                    list.add(search + patSize)
                }
                else -> {
                    list.add(search - 1)
                    list.add(search + patSize)
                }
            }
            startIndex = search + 1
        }
        if (!list.isEmpty() && list.last != (autoByteArray.size - 1)) {
            list.add(autoByteArray.size - 1)
        }



        return list
    }


    /**
     * 从索引位置开始读取一行
     *
     * @param index Long 索引位置
     * @param charset Charset 编码
     * @return String? 返回字符串（null 表示没有下一行）
     */
    fun readLine(index: Long = 0, charset: Charset = Charsets.UTF_8): String? {
        if (index >= autoByteArray.size) {
            throw java.lang.IndexOutOfBoundsException("startIndex >= autoByteArray.size")
        }
        var startIndex = index
        var char = autoByteArray[startIndex].toChar()
        if (char == '\r' || char == '\n') {
            startIndex++
            char = autoByteArray[startIndex].toChar()
            if (char == '\r' || char == '\n') {
                return ""
            }
        }
        val l = readLineEndIndex(startIndex)
        if (l == -1L) {
            return null
        }
        return autoByteArray.toByteArray(startIndex, l).toString(charset)
    }

    fun readLineEndIndex(index: Long = 0): Long {
        if (index >= autoByteArray.size) {
            throw java.lang.IndexOutOfBoundsException("startIndex >= autoByteArray.size")
        }
        var startIndex = index
        var char = autoByteArray[startIndex].toChar()
        if (char == '\r' || char == '\n') {
            startIndex++
            char = autoByteArray[startIndex].toChar()
            if (char == '\r' || char == '\n') {
                return index
            }
        }
        val rSearch = byteSearch('\r'.toByte(), startIndex)
        val nSearch = byteSearch('\n'.toByte(), startIndex)
        if (rSearch == -1L && nSearch == -1L) {
            return -1
        }
        if (rSearch == -1L) {
            return nSearch
        }
        if (nSearch == -1L) {
            return rSearch
        }
        return if (rSearch > nSearch) nSearch else rSearch
    }


    private fun byteSearch(pat: Byte, index: Long): Long {
        var startIndex = index
        while (startIndex < autoByteArray.size) {
            if (autoByteArray[startIndex] == pat) {
                return startIndex - 1
            } else {
                startIndex++
            }
        }
        return -1
    }

    private fun getSundayIndex(pat: ByteArray, byte: Byte): Int {
        for (i in pat.size - 1 downTo 0) {
            if (pat[i] == byte) {
                return i
            }
        }
        return -1
    }


    @Throws(IndexOutOfBoundsException::class)
    private fun sundaySearch(pat: ByteArray, index: Long = 0): Long {
        val m = autoByteArray.size
        val n = pat.size
        if (m < 0) {
            throw IndexOutOfBoundsException()
        }
        var i = index
        var j: Int
        var skip = -1
        while (i <= m - n) {
            j = 0
            while (j < n) {
                if (autoByteArray[i + j] != pat[j]) {
                    if (i == m - n) {
                        break
                    }
                    skip = n - getSundayIndex(pat, autoByteArray[i + n])
                    break
                }
                j++
            }
            if (j == n) {
                return i
            }
            i += skip
        }
        return -1
    }

    fun calculate(method: String): String = IOUtils.calculatedHash(autoByteArray.openInputStream(), method)

    fun readLines(index: Long, lineSize: Int): Long {
        var result = index
        for (i in 0 .. lineSize) {
            result = readLineEndIndex(result + 1)
            if (result == -1L) {
                return -1L
            }
        }
        for (i in 0 .. 1){
            val l = result + 1
            if (l < autoByteArray.size){
                var char = autoByteArray[l].toChar()
                if (char == '\r' || char == '\n') {
                    result++
                }
            }else{
                break
            }
        }
        return result + 1

    }









}
