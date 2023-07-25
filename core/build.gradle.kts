plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.kapt)
    id("android-template-convention")
}

android {
    namespace = "com.glebalekseevjk.core"
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.data.binding)
    implementation(libs.swipe.refresh.layout)
    implementation(libs.retrofit)
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)
}