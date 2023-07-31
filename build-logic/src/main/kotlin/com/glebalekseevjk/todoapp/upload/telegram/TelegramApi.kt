package com.glebalekseevjk.todoapp.upload.telegram

import okhttp3.Authenticator
import okhttp3.Credentials
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.asRequestBody
import org.jetbrains.kotlin.com.google.gson.Gson
import java.io.File
import java.net.InetSocketAddress
import java.net.Proxy


object TelegramApi {
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

    fun uploadFile(file: File, token: String, chatId: String, fileName: String) {
        val body = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("chat_id", chatId)
            .addFormDataPart(
                "document", fileName,
                file.asRequestBody("application/octet-stream".toMediaType())
            )
            .build()
        val request = Request.Builder()
            .url("$BASE_URL/bot$token/sendDocument")
            .post(body)
            .build()
        val response = client.newCall(request).execute()
    }

    fun sendMessage(message: String, token: String, chatId: String) {
        val body = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("chat_id", chatId)
            .addFormDataPart("text", message)
            .build()
        val request = Request.Builder()
            .url("$BASE_URL/bot$token/sendMessage")
            .post(body)
            .build()

        val response = client.newCall(request).execute()
    }

    fun getChatIds(token: String): List<Long> {
        val request = Request.Builder()
            .url("$BASE_URL/bot$token/getUpdates")
            .build()
        val jsonResponse = client.newCall(request).execute().body?.string()!!
        val chats = Gson().fromJson(jsonResponse, Chats::class.java)
        return chats.result.map { it.message?.chat?.id }.filterNotNull()
    }

    private const val BASE_URL = "https://api.telegram.org"
}

data class Chats(
    val ok: Boolean,
    val result: List<Chat>
)

data class Chat(
    val message: Message?,
)

data class Message(
    val chat: ChatInfo?
)

data class ChatInfo(
    val id: Long?
)