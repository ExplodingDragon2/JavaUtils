package jdkUtils.data

import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.nio.charset.Charset


object StringUtils {

    @JvmStatic
    fun readInputStream(inputStream: InputStream,charset: Charset = Charsets.UTF_8): String  = inputStream.readBytes().toString(charset)

    fun throwableFormat(throws:Throwable):String{
        val outputStream = ByteArrayOutputStream()
        val printWriter = PrintWriter(OutputStreamWriter(outputStream,Charsets.UTF_8),true)
        throws.printStackTrace(printWriter)
        printWriter.flush()
        val array = outputStream.toByteArray()
        printWriter.close()
        outputStream.close()
        return array.toString(Charsets.UTF_8).trim()
    }


}
