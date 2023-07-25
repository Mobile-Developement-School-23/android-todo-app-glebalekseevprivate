plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("android-template-convention")
}

android {
    namespace = "com.glebalekseevjk.auth.data"
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":auth:domain"))
    implementation(project(":core"))

    implementation(libs.dagger)
    implementation(libs.lifecycle.runtime)
}