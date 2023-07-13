import dependencies.Dependencies
plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.glebalekseevjk.data.sync"
    compileSdk = Config.COMPILE_SDK

    defaultConfig {
        minSdk = Config.MIN_SDK
        targetSdk = Config.TARGET_SDK

        testInstrumentationRunner = Config.TEST_INSTRUMENTATION_RUNNER
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(project(":domain:sync"))
    implementation(project(":domain:todoitem"))
    implementation(project(":common"))
    implementation(project(":core:preferences"))
    implementation(project(":core:room"))
    implementation(project(":core:retrofit"))
    implementation(project(":core:utils"))
    implementation(project(":design"))

    implementation(Dependencies.Dagger.DAGGER)
    kapt(Dependencies.Dagger.DAGGER_COMPILER)
    implementation(Dependencies.Retrofit.RETROFIT)
    implementation(Dependencies.Lifecycle.LIFECYCLE_RUNTIME)
    testImplementation(Dependencies.Test.JUNIT)
    androidTestImplementation(Dependencies.AndroidTest.ANDROID_JUNIT)
}