package jdkUtils.io

import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.File

/**
 * @author Explo
 */
class FileUtilsTest {
    @Before
    fun before() {
        println(File("resources/test").absolutePath)
    }


    @Test
    fun lengthFormat() {
        Assert.assertThat(FileUtils.lengthFormat(1024, 1), equalTo("1.0KB"))
        Assert.assertThat(FileUtils.lengthFormat(1024, 0), equalTo("1KB"))
        Assert.assertThat(FileUtils.lengthFormat(4096, 0), equalTo("4KB"))
        Assert.assertThat(FileUtils.lengthFormat(1024 * 1024, 0), equalTo("1MB"))
        Assert.assertThat(FileUtils.lengthFormat(1024 * 1024 * 1024, 0), equalTo("1GB"))
    }


}