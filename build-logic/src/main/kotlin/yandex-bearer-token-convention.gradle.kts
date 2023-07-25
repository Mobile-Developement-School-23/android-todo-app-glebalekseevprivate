import org.jetbrains.kotlin.konan.properties.Properties

android {
    val localPropertiesFile = rootProject.file("local.properties")
    val localProperties = Properties()
    if (localPropertiesFile.exists()) {
        localProperties.load(localPropertiesFile.inputStream())
    }
    defaultConfig {
        buildConfigField("String", "YANDEX_BEARER_TOKEN",
            "\"${localProperties.getProperty("yandex_bearer_token", "")}\""
        )
    }
    @Suppress("UnstableApiUsage")
    buildFeatures {
        buildConfig = true
    }
}


fun Project.android(configure: Action<com.android.build.gradle.LibraryExtension>): Unit =
    (this as ExtensionAware).extensions.configure("android", configure)
