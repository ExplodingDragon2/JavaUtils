package jdkUtils.io.autoByteArray

import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset

/**
 * @author ExplodingDragon
 * @version 1.0
 */
class DefaultAutoByteArray : AutoByteArray {
    override val size: Long
        get() = 0

    override fun get(l: Long): Byte {
        return 0
    }

    override fun openInputStream(): InputStream {
        throw RuntimeException("Stub !")
    }

    @Throws(IOException::class)
    override fun close() {

    }

    override fun toString(offset: Long, length: Int, charset: Charset): String {
        throw RuntimeException("Stub !")
    }
}
