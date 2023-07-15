import dependencies.Dependencies

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs")
    id("kotlin-kapt")
}

android {
    namespace = "com.glebalekseevjk.feature.todoitem"
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
    buildFeatures {
        viewBinding = true
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.6"
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation ("androidx.compose.ui:ui:1.4.3")
    implementation ("androidx.compose.ui:ui-tooling-preview:1.4.3")
    implementation("androidx.compose.foundation:foundation:1.4.3")
//    implementation ("androidx.activity:activity-compose:$compose_version")
    implementation ("androidx.compose.material:material:1.4.3")
    implementation ("androidx.compose.material3:material3:1.1.1")
//    implementation ("androidx.compose.material3:material3:1.1.0-beta02")
//    implementation ("androidx.navigation:navigation-compose:2.6.0-alpha09")

    implementation(project(":core:utils"))
    implementation(project(":core:room"))
    implementation(project(":common"))
    implementation(project(":core:retrofit"))
    implementation(project(":core:preferences"))
    implementation(project(":domain:auth"))
    implementation(project(":data:todoitem"))
    implementation(project(":domain:sync"))
    implementation(project(":domain:todoitem"))
    implementation(project(":design"))

    implementation("androidx.coordinatorlayout:coordinatorlayout:1.2.0")

    implementation(Dependencies.SwipeRefreshLayout.SWIPE_REFRESH_LAYOUT)
    implementation(Dependencies.Material.MATERIAL)
    implementation(Dependencies.AppCompat.APP_COMPAT)
    implementation(Dependencies.ConstraintLayout.CONSTRAINT_LAYOUT)
    implementation(Dependencies.Core.CORE_KTX)
    implementation(Dependencies.Dagger.DAGGER)
    kapt(Dependencies.Dagger.DAGGER_COMPILER)
    implementation(Dependencies.Navigation.NAVIGATION_FRAGMENT)
    implementation(Dependencies.Navigation.NAVIGATION_UI)
    implementation(Dependencies.Material.MATERIAL)
    testImplementation(Dependencies.Test.JUNIT)
    androidTestImplementation(Dependencies.AndroidTest.ANDROID_JUNIT)
}