// Top-level build file where you can add configuration options common to all sub-projects/modules.

plugins {
    kotlin("jvm") version "1.7.20" apply false
}
val ktVersion by extra { "1.7.0" }
val appcompatVersion by extra { "1.5.1" }
buildscript {

    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
    dependencies {
        classpath ("com.android.tools.build:gradle:7.0.2")
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.21")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }
}


tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}