package jdkUtils.data

import org.junit.Test

class StringUtilsTest{
    @Test
    fun test() {
        val byteArrayOf = byteArrayOf(127, 21)
        println((byteArrayOf[0] + 128).toString(16))
    }


}