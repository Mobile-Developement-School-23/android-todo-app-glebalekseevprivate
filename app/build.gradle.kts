import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.navigation.safe.args)
    alias(libs.plugins.kotlin.kapt)
    id("upload-to-telegram-plugin")
}

uploadToTelegramConfig {
    val localPropertiesFile = rootProject.file("local.properties")
    val localProperties = Properties()
    if (localPropertiesFile.exists()) {
        localProperties.load(localPropertiesFile.inputStream())
    }

    isValidateApkSizeEnabled = true
    isAPKAnalysisEnabled = true
    maxApkSize = 13*1024

    telegramBotToken = localProperties.getProperty("telegram_bot_token", "")
    proxyHost = localProperties.getProperty("proxy_host", "")
    proxyPort = localProperties.getProperty("proxy_port", "").toInt()
    proxyLogin = localProperties.getProperty("proxy_login", "")
    proxyPassword = localProperties.getProperty("proxy_password", "")
}

android {
    namespace = "com.glebalekseevjk.todoapp"

    val localPropertiesFile = rootProject.file("local.properties")
    val localProperties = Properties()
    if (localPropertiesFile.exists()) {
        localProperties.load(localPropertiesFile.inputStream())
    }
    compileSdk = 33

    defaultConfig {
        applicationId = "com.glebalekseevjk.todoapp"
        versionCode = 1
        versionName = "1.0"
        minSdk = 26
        targetSdk = 33
        manifestPlaceholders["YANDEX_CLIENT_ID"] =
            localProperties.getProperty("yandex_client_id", "")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":auth:data"))
    implementation(project(":auth:presentation"))
    implementation(project(":auth:domain"))
    implementation(project(":todo:presentation"))
    implementation(project(":todo:data"))
    implementation(project(":todo:domain"))
    implementation(project(":todo:todoedit:presentation"))

    implementation(libs.appcompat)
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)
    implementation(libs.work)
    implementation(libs.work.ktx)
    implementation(libs.lifecycle.runtime)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.room)
    implementation(libs.room.ktx)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.logging.interceptor.okhttp)
}