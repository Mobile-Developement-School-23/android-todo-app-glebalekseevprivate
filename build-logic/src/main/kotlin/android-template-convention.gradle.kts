android {
    compileSdk = 33

    defaultConfig {
        minSdk = 26
        targetSdk = 33

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
}

dependencies {
    val testImplementation by configurations
    val androidTestImplementation by configurations
    testImplementation(libs.junit)
    androidTestImplementation(libs.android.junit)
}

fun Project.android(configure: Action<com.android.build.gradle.LibraryExtension>): Unit =
    (this as ExtensionAware).extensions.configure("android", configure)

val Project.libs: org.gradle.accessors.dm.LibrariesForLibs get() =
    (this as ExtensionAware).extensions.getByName("libs") as org.gradle.accessors.dm.LibrariesForLibs