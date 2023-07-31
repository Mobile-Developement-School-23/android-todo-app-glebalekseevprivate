package com.glebalekseevjk.todoapp.upload.telegram

import com.glebalekseevjk.todoapp.firstApk
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

abstract class ValidateApkSizeTask : DefaultTask() {
    @get:InputDirectory
    abstract val apkDir: DirectoryProperty

    @get:Input
    abstract val apkName: Property<String>

    @get:Input
    abstract val maxApkSize: Property<Int>

    @get:OutputFile
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
    fun validateApkSize() {
        TelegramApi.setupProxy(
            proxyHost.orNull,
            proxyPort.orNull,
            proxyLogin.orNull,
            proxyPassword.orNull
        )
        val token = telegramBotToken.get()
        val chatIds = TelegramApi.getChatIds(token)
        val apk = apkDir.firstApk
        val apkName = apkName.get()
        val maxApkSize = maxApkSize.get()
        val fileSizeInKB = apk.length() / 1024
        if (fileSizeInKB > maxApkSize) {
            chatIds.distinct().forEach {
                TelegramApi.sendMessage(
                    "APK $apkName exceeds the maximum size: $fileSizeInKB KB > $maxApkSize KB.",
                    token,
                    it.toString()
                )
            }
            throw IllegalStateException("APK $apkName exceeds the maximum size.")
        }
        val resultText = "$apkName: $fileSizeInKB KB\n"
        apkSizeResultFile.get().asFile.writeText(resultText)
    }
}