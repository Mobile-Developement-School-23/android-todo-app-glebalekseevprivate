package com.glebalekseevjk.todoapp.upload.telegram

import com.glebalekseevjk.todoapp.exec
import com.glebalekseevjk.todoapp.firstApk
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class APKAnalysisTask : DefaultTask() {
    @get:InputDirectory
    abstract val apkDir: DirectoryProperty

    @get:Input
    abstract val apkName: Property<String>

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
    fun analysis() {
        TelegramApi.setupProxy(
            proxyHost.orNull,
            proxyPort.orNull,
            proxyLogin.orNull,
            proxyPassword.orNull
        )
        ToolDownloader.setupProxy(
            proxyHost.orNull,
            proxyPort.orNull,
            proxyLogin.orNull,
            proxyPassword.orNull
        )
        val token = telegramBotToken.get()
        val chatIds = TelegramApi.getChatIds(token)
        val apk = apkDir.firstApk
        val apkName = apkName.get()
        val pathToApktool = ToolDownloader.downloadApkTool()
        "java -jar $pathToApktool d ${apk.absolutePath} -sf -o $APK_TOOL_OUTPUT_DIR".exec()
        val stringBuilder = StringBuilder()
        val apkSize = apk.length()
        stringBuilder.append("APK Name: $apkName\n")
        stringBuilder.append("APK Size: ${formatSize(apkSize)}\n")
        stringBuilder.append("APK Reversed Contents:\n")
        val reversedFiles = File(APK_TOOL_OUTPUT_DIR)
        reversedFiles
            .listFiles()
            ?.sortedByDescending { it.length() }
            ?.filter { it.name !in badNames }
            ?.forEach {
                stringBuilder.append(" - ${it.name} ${formatSize(it.length())}\n")
            }
        chatIds.distinct().forEach {
            TelegramApi.sendMessage(
                stringBuilder.toString(),
                token,
                it.toString()
            )
        }
    }

    private fun formatSize(size: Long): String {
        val unit = 1024
        if (size < unit) return "$size B"
        val exp = (Math.log(size.toDouble()) / Math.log(unit.toDouble())).toInt()
        val pre = "KMGTPE"[exp - 1] + "i"
        return String.format("%.1f %sB", size / Math.pow(unit.toDouble(), exp.toDouble()), pre)
    }

    companion object {
        const val APK_TOOL_OUTPUT_DIR = "./app/build/reverse"
        val badNames = listOf("original", "unknown", "kotlin", "apktool.yml", "META-INF")
    }
}