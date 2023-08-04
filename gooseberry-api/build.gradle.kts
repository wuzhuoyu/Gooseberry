plugins {
    id("com.android.library")
    kotlin("android")
    `maven-publish`
}
val ktVersion: String by rootProject.extra
val appcompatVersion: String by rootProject.extra

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("gooseberry-api") {
                groupId = "com.yuu.android.component"
                artifactId = "gooseberry-api"
                version = "0.0.1"
                from(components["release"])
            }
        }
    }
}


android {
    compileSdk = 30
    buildToolsVersion = "30.0.3"

    defaultConfig {
        minSdk = 21
        targetSdk = 30
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true

    }

    sourceSets {
        getByName("main") {
            manifest.srcFile("src/main/AndroidManifest.xml")
        }
    }

    buildTypes {
        getByName("release") {
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
        getByName("debug") {
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    packagingOptions {
        exclude("META-INF/LICENSE.txt")
        exclude("META-INF/NOTICE.txt")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    api(project(":gooseberry-annotation"))
    implementation("org.jetbrains.kotlin:kotlin-reflect:${ktVersion}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:${ktVersion}")
    implementation("androidx.core:core-ktx:${ktVersion}")
    implementation("androidx.appcompat:appcompat:${appcompatVersion}")
    implementation("com.google.android.material:material:1.4.0")

    //gson
    api("com.google.code.gson:gson:2.8.6")
}
