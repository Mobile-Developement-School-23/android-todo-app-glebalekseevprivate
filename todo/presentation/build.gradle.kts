plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.navigation.safe.args)
    id("android-template-convention")
}

android {
    namespace = "com.glebalekseevjk.todo.presentation"
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":todo:todoedit:presentation"))
    implementation(project(":todo:todolist:presentation"))
    implementation(project(":todo:domain"))
    implementation(project(":auth:domain"))

    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.appcompat)
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)
}