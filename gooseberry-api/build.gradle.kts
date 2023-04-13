plugins {
    id("com.android.library")
    kotlin("android")
    `maven-publish`
}
afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
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
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.5.21")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.5.21")
    implementation("androidx.core:core-ktx:1.3.2")
    implementation("androidx.appcompat:appcompat:1.3.0")
    implementation("com.google.android.material:material:1.3.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")

    //gson
    api("com.google.code.gson:gson:2.8.6")
}
