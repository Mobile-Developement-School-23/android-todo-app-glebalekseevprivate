plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.library)
    id("android-template-convention")
}

android {
    namespace = "com.glebalekseevjk.todo.domain"
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(libs.lifecycle.runtime)
    implementation(libs.dagger)
}