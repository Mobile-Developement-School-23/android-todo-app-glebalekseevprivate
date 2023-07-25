plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("android-template-convention")
}

android {
    namespace = "com.glebalekseevjk.auth.domain"
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":todo:domain"))

    implementation(libs.dagger)
    implementation(libs.lifecycle.runtime)
}