plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.navigation.safe.args)
    id("android-template-convention")
}

android {
    namespace = "com.glebalekseevjk.todo.todoedit.presentation"
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.6"
    }
}

dependencies {
    implementation(project(":design"))
    implementation(project(":core"))
    implementation(project(":todo:domain"))
    implementation(project(":auth:domain"))

    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material)
    implementation(libs.compose.material3)

    implementation(libs.material)
    implementation(libs.coordinatorlayout)
    implementation(libs.constraint.layout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.runtime)
    implementation(libs.appcompat)
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)
}