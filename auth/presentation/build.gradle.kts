plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    id("android-template-convention")
    id("yandex-bearer-token-convention")
}

android {
    namespace = "com.glebalekseevjk.auth.presentation"
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }
}


dependencies {
    implementation(project(":auth:domain"))
    implementation(project(":core"))
    implementation(project(":design"))

    implementation(libs.dagger)
    kapt(libs.dagger.compiler)
    implementation(libs.yandex.auth)
    implementation(libs.appcompat)
    implementation(libs.constraint.layout)
    implementation(libs.material)
    implementation(libs.lifecycle.runtime)
}