plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.library)
    id("android-template-convention")
}

android {
    namespace = "com.glebalekseevjk.design"
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(libs.material)
    implementation(libs.appcompat)
}