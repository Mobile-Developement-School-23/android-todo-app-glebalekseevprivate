package com.glebalekseevjk.todoapp.upload.telegram

import com.glebalekseevjk.todoapp.firstApk
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

abstract class UploadToTelegramTask : DefaultTask() {
    @get:InputDirectory
    abstract val apkDir: DirectoryProperty

    @get:Input
    abstract val apkName: Property<String>

    @get:Optional
    @get:InputFile
    abstract val apkSizeResultFile: RegularFileProperty

    @get:Input
    abstract val telegramBotToken: Property<String>

    @get:Optional
    @get:Input
    abstract val proxyHost: Property<String>

    @get:Optional
    @get:Input
    abstract val proxyPort: Property<Int>

    @get:Optional
    @get:Input
    abstract val proxyLogin: Property<String>

    @get:Optional
    @get:Input
    abstract val proxyPassword: Property<String>

    @TaskAction
    fun upload() {
        TelegramApi.setupProxy(
            proxyHost.orNull,
            proxyPort.orNull,
            proxyLogin.orNull,
            proxyPassword.orNull
        )
        val token = telegramBotToken.get()
        val apk = apkDir.firstApk
        val apkName = apkName.get()
        val chatIds = TelegramApi.getChatIds(token)
        apkSizeResultFile.orNull?.let { resultFile ->
            chatIds.distinct().forEach {
                TelegramApi.sendMessage(
                    resultFile.asFile.readText(),
                    token,
                    it.toString()
                )
                TelegramApi.uploadFile(apk, token, it.toString(), apkName)
            }
        }
    }
}