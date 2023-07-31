plugins {
    `kotlin-dsl`
}

gradlePlugin {
    plugins.register("upload-to-telegram-plugin") {
        id = "upload-to-telegram-plugin"
        implementationClass = "com.glebalekseevjk.todoapp.upload.telegram.UploadToTelegramPlugin"
    }
}

dependencies {
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    implementation(libs.kotlin.gradle.plugin)
    implementation(libs.agp)
    implementation(libs.bundles.ktor)
    implementation(libs.retrofit)
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}