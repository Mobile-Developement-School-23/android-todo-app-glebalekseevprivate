package com.glebalekseevjk.todoapp.upload.telegram

import com.android.build.api.artifact.SingleArtifact
import com.glebalekseevjk.todoapp.android
import com.glebalekseevjk.todoapp.androidComponents
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.provider.Provider
import java.util.Locale

class UploadToTelegramPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.extensions.create("uploadToTelegramConfig", UploadToTelegramExtension::class.java)
        project.androidComponents.onVariants { variant ->
            val versionCode = project.android.defaultConfig.versionCode
            val variantName = variant.name.capitalize()
            val apkDir = variant.artifacts.get(SingleArtifact.APK)
            val apkName = "todolist-${variantName.lowercase(Locale.getDefault())}-$versionCode.apk"
            val proxyHost = project.uploadToTelegramExtension.proxyHost
            val proxyPort = project.uploadToTelegramExtension.proxyPort
            val proxyLogin = project.uploadToTelegramExtension.proxyLogin
            val proxyPassword = project.uploadToTelegramExtension.proxyPassword
            val telegramBotToken = project.uploadToTelegramExtension.telegramBotToken
            val isValidateApkSizeEnabled =
                project.uploadToTelegramExtension.isValidateApkSizeEnabled
            val isAPKAnalysisEnabled = project.uploadToTelegramExtension.isAPKAnalysisEnabled

            val uploadApkToTelegramTask = project.registerUploadApkToTelegramTask(
                variantName,
                apkDir,
                apkName,
                telegramBotToken,
                proxyHost,
                proxyPort,
                proxyLogin,
                proxyPassword
            )
            if (isValidateApkSizeEnabled) {
                val validateApkSizeTask = project.registerValidateApkSizeTask(
                    variantName,
                    apkDir,
                    apkName,
                    telegramBotToken,
                    proxyHost,
                    proxyPort,
                    proxyLogin,
                    proxyPassword
                )
                uploadApkToTelegramTask.configure {
                    dependsOn(validateApkSizeTask)
                    uploadApkToTelegramTask.get().apkSizeResultFile.set(validateApkSizeTask.get().apkSizeResultFile)
                }
            }
            if (isAPKAnalysisEnabled) {
                val apkAnalysisTask = registerAPKAnalysisTask(
                    project,
                    variantName,
                    apkDir,
                    apkName,
                    telegramBotToken,
                    proxyHost,
                    proxyPort,
                    proxyLogin,
                    proxyPassword
                )
                uploadApkToTelegramTask.configure {
                    finalizedBy(apkAnalysisTask)
                }
            }
        }
    }

    private fun Project.registerUploadApkToTelegramTask(
        variantName: String,
        apkDir: Provider<Directory>,
        apkName: String,
        telegramBotToken: String,
        proxyHost: String?,
        proxyPort: Int?,
        proxyLogin: String?,
        proxyPassword: String?,
    ) = project.tasks.register(
        "uploadApkToTelegramFor$variantName",
        UploadToTelegramTask::class.java
    ) {
        this.apkDir.set(apkDir)
        this.apkName.set(apkName)
        this.telegramBotToken.set(telegramBotToken)
        proxyHost?.let { this.proxyHost.set(it) }
        proxyPort?.let { this.proxyPort.set(it) }
        proxyLogin?.let { this.proxyLogin.set(it) }
        proxyPassword?.let { this.proxyPassword.set(it) }
    }

    private fun Project.registerValidateApkSizeTask(
        variantName: String,
        apkDir: Provider<Directory>,
        apkName: String,
        telegramBotToken: String,
        proxyHost: String?,
        proxyPort: Int?,
        proxyLogin: String?,
        proxyPassword: String?,
    ) = project.tasks.register(
        "validateApkSizeFor$variantName",
        ValidateApkSizeTask::class.java
    ) {
        this.apkDir.set(apkDir)
        this.apkName.set(apkName)
        this.maxApkSize.set(project.uploadToTelegramExtension.maxApkSize)
        this.apkSizeResultFile.set(project.layout.buildDirectory.file("outputs/apkSizeResult.txt"))
        this.telegramBotToken.set(telegramBotToken)
        proxyHost?.let { this.proxyHost.set(it) }
        proxyPort?.let { this.proxyPort.set(it) }
        proxyLogin?.let { this.proxyLogin.set(it) }
        proxyPassword?.let { this.proxyPassword.set(it) }
    }

    private fun registerAPKAnalysisTask(
        project: Project,
        variantName: String,
        apkDir: Provider<Directory>,
        apkName: String,
        telegramBotToken: String,
        proxyHost: String?,
        proxyPort: Int?,
        proxyLogin: String?,
        proxyPassword: String?,
    ) =
        project.tasks.register(
            "analysisAPKFor$variantName",
            APKAnalysisTask::class.java
        ) {
            this.apkDir.set(apkDir)
            this.apkName.set(apkName)
            this.telegramBotToken.set(telegramBotToken)
            proxyHost?.let { this.proxyHost.set(it) }
            proxyPort?.let { this.proxyPort.set(it) }
            proxyLogin?.let { this.proxyLogin.set(it) }
            proxyPassword?.let { this.proxyPassword.set(it) }
        }
}

private val Project.uploadToTelegramExtension
    get() =
        project.extensions.getByType(UploadToTelegramExtension::class.java)

/**
 * @property maxApkSize Максимальный размер apk в КБ
 * @property isValidateApkSizeEnabled Включить валидацию размера apk
 * @property isAPKAnalysisEnabled Включить анализ apk
 * @property telegramBotToken Токен бота
 * @property isProxyEnabled Включить прокси
 * @property proxyHost Хост прокси
 * @property proxyPort Порт прокси
 * @property proxyLogin Логин прокси
 * @property proxyPassword Пароль прокси
 */
abstract class UploadToTelegramExtension {
    var maxApkSize: Int = DEFAULT_MAX_APK_SIZE
    var isValidateApkSizeEnabled: Boolean = false
    var isAPKAnalysisEnabled: Boolean = false

    abstract var telegramBotToken: String
    var proxyHost: String? = null
    var proxyPort: Int? = null
    var proxyLogin: String? = null
    var proxyPassword: String? = null

    companion object {
        const val DEFAULT_MAX_APK_SIZE = 10 * 1024
    }
}