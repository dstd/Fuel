package com.github.kittinunf.fuel

import com.github.kittinunf.fuel.core.*
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import java.io.File
import java.io.FileNotFoundException
import java.net.HttpURLConnection
import java.util.concurrent.CountDownLatch
import org.hamcrest.CoreMatchers.`is` as isEqualTo

class RequestUploadTest : BaseTestCase() {

    val manager: Manager by lazy {
        Manager().apply {
            basePath = "http://httpbin.org"
        }
    }

    val currentDir: File by lazy {
        val dir = System.getProperty("user.dir")
        File(dir, "src/test/assets")
    }

    @Before
    fun setUp() {
        lock = CountDownLatch(1)
    }

    @Test
    fun httpUploadWithPostCase() {
        var request: Request? = null
        var response: Response? = null
        var data: Any? = null
        var error: FuelError? = null

        manager.upload("/post").source { request, url ->
            File(currentDir, "lorem_ipsum_short.tmp")
        }.responseString { req, res, result ->
            request = req
            response = res
            val (d, err) = result
            data = d
            error = err

            lock.countDown()
        }

        await()

        assertThat(request, notNullValue())
        assertThat(response, notNullValue())
        assertThat(error, nullValue())
        assertThat(data, notNullValue())

        val statusCode = HttpURLConnection.HTTP_OK
        assertThat(response?.httpStatusCode, isEqualTo(statusCode))
    }

    @Test
    fun httpUploadWithPutCase() {
        var request: Request? = null
        var response: Response? = null
        var data: Any? = null
        var error: FuelError? = null

        manager.upload("/put", Method.PUT).source { request, url ->
            File(currentDir, "lorem_ipsum_long.tmp")
        }.responseString { req, res, result ->
            request = req
            response = res
            val (d, err) = result
            data = d
            error = err

            lock.countDown()
        }

        await()

        assertThat(request, notNullValue())
        assertThat(response, notNullValue())
        assertThat(error, nullValue())
        assertThat(data, notNullValue())

        val statusCode = HttpURLConnection.HTTP_OK
        assertThat(response?.httpStatusCode, isEqualTo(statusCode))
    }

    @Test
    fun httpUploadWithProgressValidCase() {
        var request: Request? = null
        var response: Response? = null
        var data: Any? = null
        var error: FuelError? = null

        var read = -1L
        var total = -1L

        manager.upload("/post").source { request, url ->
            File(currentDir, "lorem_ipsum_long.tmp")
        }.progress { readBytes, totalBytes ->
            read = readBytes
            total = totalBytes
            println("read: $read, total: $total")
        }.responseString { req, res, result ->
            request = req
            response = res
            val (d, err) = result
            data = d
            error = err

            lock.countDown()
        }

        await()

        assertThat(request, notNullValue())
        assertThat(response, notNullValue())
        assertThat(error, nullValue())
        assertThat(data, notNullValue())

        val statusCode = HttpURLConnection.HTTP_OK
        assertThat(response?.httpStatusCode, isEqualTo(statusCode))

        assertThat(read == total && read != -1L && total != -1L, isEqualTo(true))
    }

    @Test
    fun httpUploadWithProgressInvalidEndPointCase() {
        var request: Request? = null
        var response: Response? = null
        var data: Any? = null
        var error: FuelError? = null

        manager.upload("/pos").source { request, url ->
            File(currentDir, "lorem_ipsum_short.tmp")
        }.progress { readBytes, totalBytes ->

        }.responseString { req, res, result ->
            request = req
            response = res
            val (d, err) = result
            data = d
            error = err

            lock.countDown()
        }

        await()

        assertThat(request, notNullValue())
        assertThat(response, notNullValue())
        assertThat(error, notNullValue())
        assertThat(data, nullValue())

        val statusCode = HttpURLConnection.HTTP_NOT_FOUND
        assertThat(response?.httpStatusCode, isEqualTo(statusCode))
    }

    @Test
    fun httpUploadWithProgressInvalidFileCase() {
        var request: Request? = null
        var response: Response? = null
        var data: Any? = null
        var error: FuelError? = null

        manager.upload("/post").source { request, url ->
            File(currentDir, "not_found_file.tmp")
        }.progress { readBytes, totalBytes ->

        }.responseString { req, res, result ->
            request = req
            response = res
            val (d, err) = result
            data = d
            error = err

            lock.countDown()
        }

        await()

        assertThat(request, notNullValue())
        assertThat(response, notNullValue())
        assertThat(error, notNullValue())
        assertThat(data, nullValue())

        assertThat(error?.exception as FileNotFoundException, isA(FileNotFoundException::class.java))

        val statusCode = -1
        assertThat(response?.httpStatusCode, isEqualTo(statusCode))
    }

}
