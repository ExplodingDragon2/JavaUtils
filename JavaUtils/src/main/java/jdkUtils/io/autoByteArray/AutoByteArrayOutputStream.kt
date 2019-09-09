package jdkUtils.io.autoByteArray

import jdkUtils.data.StringUtils
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import kotlin.random.Random

/**
 * @author ExplodingDragon
 * @version 1.0
 */
class AutoByteArrayOutputStream(private val tempFile: File = getTempFile("${Random.nextDouble()}"), private val maxByteArraySize: Int = 4096) : OutputStream() {
    @Volatile
    private var isClose: Boolean = false
    private var byteArray = ByteArray(32)
    private var byteArraySize = 0
    var useByteArray: Boolean = tempFile.isFile.not()
    private set

    private val tempFileOutputStream by lazy {
        FileOutputStream(tempFile)
    }

    /**
     * 已知长度
     */
    val size: Long
        get() = if (useByteArray) {
            byteArraySize.toLong()
        } else {
            tempFile.length()
        }

    val autoByteArray by lazy {
        close()
        if (useByteArray){
             ArrayAutoByteArray(byteArray.copyOf(byteArraySize))
        }else{
             FileAutoByteArray(tempFile)
        }
    }



    @Synchronized
    override fun write(b: ByteArray, off: Int, len: Int) {
        if (isClose) {
            throw RuntimeException("This is already closed.")
        }
        if (off < 0 || off > b.size || len < 0 ||
                off + len > b.size || off + len < 0) {
            throw IndexOutOfBoundsException()
        } else if (len == 0) {
            return
        }
        if (useByteArray) {
            writeByteArray(b, 0, len)
        } else {
            writeFile(b, 0, len)
        }

    }

    private fun writeFile(b: ByteArray, i: Int, size: Int) {
        tempFileOutputStream.write(b, i, size)
        tempFileOutputStream.flush()
    }

    private fun writeByteArray(b: ByteArray, i: Int, size: Int) {
        val newSize = byteArraySize + size
        if (newSize >= maxByteArraySize) {
            writeFile(byteArray, 0, byteArraySize)
            byteArray = ByteArray(0)
            useByteArray = false
            writeFile(b, i, size)
            byteArraySize = 0
            return

        } else if (byteArray.size < newSize) {
            byteArray = byteArray.copyOf(newSize)
        }
        System.arraycopy(b, i, byteArray, byteArraySize, size)
        byteArraySize = newSize
    }


    @Synchronized
    override fun close() {
        if (!isClose) {
            isClose = true
            if (!useByteArray) {
                tempFileOutputStream.close()
            }
        }
    }

    override fun write(b: Int) {
        write(byteArrayOf(b.toByte()))
    }

    override fun write(b: ByteArray) {
        write(b, 0, b.size)
    }


    companion object {
        @JvmStatic
        fun getTempFile(any: Any): File {
            val tempDir = System.getProperty("java.io.tmpdir")
            tempDir?.let {
                val name = "${any.hashCode()} - ${System.nanoTime()} - $any"
                return File(it, "MiniPlayerCore_${StringUtils.stringMd5(name)}.tmp")
            }
            throw NullPointerException("System property \"java.io.tmpdir\" == null .")
        }
    }
}
