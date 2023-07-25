plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.kapt)
    id("android-template-convention")
}

android {
    namespace = "com.glebalekseevjk.todo.data"
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":todo:domain"))
    implementation(project(":auth:domain"))

    implementation(libs.dagger)
    implementation(libs.room)
    implementation(libs.room.ktx)
    kapt(libs.room.compiler)
    implementation(libs.room.runtime)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.logging.interceptor.okhttp)
    implementation(libs.work)
    implementation(libs.work.ktx)
}