package com.github.kittinunf.fuel.toolbox

import com.github.kittinunf.fuel.core.*
import java.io.BufferedOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URLConnection
import java.util.zip.GZIPInputStream
import javax.net.ssl.HttpsURLConnection

class HttpClient : Client {
    override fun executeRequest(request: Request): Response {
        val response = Response()
        response.url = request.url

        val connection = establishConnection(request) as HttpURLConnection

        try {
            connection.apply {
                val timeout = request.timeoutInMillisecond
                connectTimeout = timeout
                readTimeout = timeout
                doInput = true
                useCaches = false
                requestMethod = request.httpMethod.value
                setDoOutput(connection, request.httpMethod)
                for ((key, value) in request.httpHeaders) {
                    setRequestProperty(key, value)
                }
                setBodyIfAny(connection, request.httpBody)
            }

            return response.apply {

                httpResponseHeaders = connection.headerFields ?: emptyMap()
                httpContentLength = connection.contentLength.toLong()

                val contentEncoding = connection.contentEncoding ?: ""

                val dataStream = if (connection.errorStream != null) {
                    connection.errorStream
                } else {
                    try {
                        connection.inputStream
                    } catch(exception: IOException) {
                        null
                    }
                }

                if (dataStream != null) {
                    data = if (contentEncoding.compareTo("gzip", true) == 0) {
                        GZIPInputStream(dataStream).readBytes()
                    } else {
                        dataStream.readBytes()
                    }
                }

                //try - catch just in case both methods throw
                try {
                    httpStatusCode = connection.responseCode
                    httpResponseMessage = connection.responseMessage
                } catch(exception: IOException) {
                    throw exception
                }
            }
        } catch(exception: Exception) {
            throw FuelError().apply {
                this.exception = exception
                this.errorData = response.data
            }
        } finally {
            connection.disconnect()
        }
    }

    private fun establishConnection(request: Request): URLConnection {
        return if (request.url.protocol.equals("https")) {
            val conn = request.url.openConnection() as HttpsURLConnection
            conn.apply {
                sslSocketFactory = request.socketFactory
                hostnameVerifier = request.hostnameVerifier
            }
        } else {
            request.url.openConnection() as HttpURLConnection
        }
    }

    private fun setBodyIfAny(connection: HttpURLConnection, bytes: ByteArray) {
        if (bytes.size != 0) {
            val outStream = BufferedOutputStream(connection.outputStream)
            outStream.write(bytes)
            outStream.close()
        }
    }

    private fun setDoOutput(connection: HttpURLConnection, method: Method) {
        when (method) {
            Method.GET, Method.DELETE -> connection.doOutput = false
            Method.POST, Method.PUT, Method.PATCH -> connection.doOutput = true
        }
    }

}


