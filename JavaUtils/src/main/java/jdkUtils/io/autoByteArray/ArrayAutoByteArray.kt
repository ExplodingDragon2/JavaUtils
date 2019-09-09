package jdkUtils.io.autoByteArray

import java.io.ByteArrayInputStream
import java.io.InputStream

/**
 * # 基于``` ByteArray ``` 的 ``` ArrayAutoByteArray```
 *
 *
 * @author ExplodingDragon
 * @version 1.0
 */

class ArrayAutoByteArray(private val byteArray: ByteArray) : AutoByteArray {
    override val size: Long
        get() = byteArray.size.toLong()

    override fun get(l: Long): Byte {
        if (l >= size){
            throw IndexOutOfBoundsException("l >= size :$l >= $size .")
        }
        val index = l.toInt()
        return byteArray[index]
    }

    override fun openInputStream(): InputStream {
        return ByteArrayInputStream(byteArray)
    }

    override fun close() {
    }

}