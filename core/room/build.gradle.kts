import dependencies.Dependencies
plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.glebalekseevjk.core.room"
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
    implementation(project(":domain:todoitem"))
    implementation(project(":common"))

    implementation(Dependencies.Dagger.DAGGER)
    implementation(Dependencies.Room.ROOM)
    implementation(Dependencies.Room.ROOM_KTX)
    implementation(Dependencies.Room.ROOM_RUNTIME)
    kapt(Dependencies.Room.ROOM_COMPILER)
    testImplementation(Dependencies.Test.JUNIT)
    androidTestImplementation(Dependencies.AndroidTest.ANDROID_JUNIT)
}