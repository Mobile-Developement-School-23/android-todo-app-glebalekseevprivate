package com.glebalekseevjk.todoapp

import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.file.DirectoryProperty
import java.security.Provider

val Project.androidComponents get() = extensions.findByType(AndroidComponentsExtension::class.java)
    ?: throw IllegalStateException("Not an Android project")

val DirectoryProperty.firstApk get() = get().asFile .listFiles()?.first { it.name.endsWith(".apk") }
    ?: throw IllegalStateException("No APKs in $asFile")

val Project.android get() = (project.extensions.getByName("android") as BaseAppModuleExtension)

fun String.exec() {
    Runtime.getRuntime().exec(this)
}