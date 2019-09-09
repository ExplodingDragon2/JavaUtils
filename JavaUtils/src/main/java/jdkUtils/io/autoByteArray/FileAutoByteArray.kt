package jdkUtils.io.autoByteArray

import jdkUtils.logcat.Logger
import java.io.*
import java.util.*

/**
 * @author ExplodingDragon
 * @version 1.0
 */
class FileAutoByteArray(val tempFile: File) : AutoByteArray {
    private val logger  = Logger.getLogger(this)
    var isClosed = false
        private set
    private val closeableList = LinkedList<Closeable>()
    override val size: Long by lazy {
        tempFile.length()
    }
    private val randomAccessFile by lazy {
        RandomAccessFile(tempFile, "r")

    }

    init {
        if (tempFile.isFile.not()) {
            throw FileNotFoundException()
        }
    }

    @Synchronized
    override fun openInputStream(): InputStream {
        if (isClosed) {
            throw RuntimeException("This is already closed.")
        }
        val fileInputStream = FileInputStream(tempFile)
        closeableList.add(fileInputStream)
        return fileInputStream
    }

    @Synchronized
    override fun get(l: Long): Byte {
        if (isClosed) {
            throw RuntimeException("This is already closed.")
        }
        if (l >= size) {
            throw IndexOutOfBoundsException("l >= size :$l >= $size .")
        }
        randomAccessFile.seek(l)
        return randomAccessFile.readByte()
    }

    @Synchronized
    override fun close() {
        if (isClosed.not()) {
            isClosed = true
        }
        closeableList.forEach {
            try {
                it.close()
            } catch (ignore: Exception) {
                logger.debug("Close Error",ignore)
            }
        }
        randomAccessFile.close()
        if (!tempFile.delete()) {
            logger.debug("file [${tempFile.absolutePath}] 删除失败！")
        }
    }

}