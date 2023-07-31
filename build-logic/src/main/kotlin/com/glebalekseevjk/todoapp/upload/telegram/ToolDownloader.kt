package com.glebalekseevjk.todoapp.upload.telegram

import okhttp3.Authenticator
import okhttp3.Credentials
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.net.InetSocketAddress
import java.net.Proxy

object ToolDownloader {
    private var client = OkHttpClient.Builder()
        .build()

    fun setupProxy(
        proxyHost: String?,
        proxyPort: Int?,
        proxyUser: String?,
        proxyPassword: String?
    ) {
        proxyHost ?: return
        proxyPort ?: return
        proxyUser ?: return
        proxyPassword ?: return
        val proxyAuthenticator = Authenticator { route, response ->
            val credential: String = Credentials.basic(proxyUser, proxyPassword)
            response.request.newBuilder()
                .header("Proxy-Authorization", credential)
                .build()
        }
        client = OkHttpClient.Builder()
            .proxy(Proxy(Proxy.Type.HTTP, InetSocketAddress(proxyHost, proxyPort)))
            .proxyAuthenticator(proxyAuthenticator)
            .build()
    }

    fun downloadApkTool(): String {
        val request = Request.Builder()
            .url(APK_TOOL_URL)
            .build()
        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            response.close()
            throw RuntimeException("Failed to download apktool")
        }

        if (!File(TOOLS_PATH).exists()) {
            File(TOOLS_PATH).mkdir()
        }

        val pathToResult = "./$TOOLS_PATH/$APK_TOOL_NAME"
        if (File(pathToResult).exists()) {
            return pathToResult
        }
        val outputStream = FileOutputStream(File(pathToResult))
        response.body?.byteStream()?.use { inputStream ->
            val buffer = ByteArray(4096)
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }
            outputStream.flush()
        }
        response.close()
        outputStream.close()

        return pathToResult
    }

    private const val APK_TOOL_NAME = "apktool.jar"
    private const val TOOLS_PATH = "tools"
    private const val APK_TOOL_URL =
        "https://github.com/iBotPeaches/Apktool/releases/download/v2.8.1/apktool_2.8.1.jar"
}