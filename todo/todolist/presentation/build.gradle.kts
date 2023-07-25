plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.navigation.safe.args)
    id("android-template-convention")
}

android {
    namespace = "com.glebalekseevjk.todo.todolist.presentation"
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(project(":design"))
    implementation(project(":core"))
    implementation(project(":todo:domain"))
    implementation(project(":auth:domain"))

    implementation(libs.swipe.refresh.layout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.material)
    implementation(libs.coordinatorlayout)
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.runtime)
    implementation(libs.appcompat)
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)
}