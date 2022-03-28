
plugins {
    id("com.android.application")
    id("kotlin-android")
    id ("com.google.devtools.ksp") version ("1.5.21-1.0.0-beta07")
}

val composeVersion = "1.0.1"

android {
    compileSdk = 30

    defaultConfig {
        applicationId = "com.yuu.gooseberry"
        minSdk = 21
        targetSdk = 30
        versionCode = 1
        versionName = "V0.0.1"
        flavorDimensions("default")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }


    buildTypes {
        getByName("release").apply {

            sourceSets {
                getByName("main") {
                    java.srcDir(File("build/generated/ksp/release/kotlin")) // 指定ksp生成目录，否则编译器不会之别生成的代码
                }
            }
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        getByName("debug").apply {
            sourceSets {
                getByName("main") {
                    java.srcDir(File("build/generated/ksp/debug/kotlin")) // 指定ksp生成目录，否则编译器不会之别生成的代码
                }
            }
        }

    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}


dependencies {
    implementation(project(":gooseberry-api"))
    //    implementation(project(":processor"))
//    ksp(project(":processor"))

    implementation ("androidx.core:core-ktx:1.3.2")
    implementation ("androidx.appcompat:appcompat:1.2.0")
    implementation ("com.google.android.material:material:1.3.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.0.4")

    testImplementation ("junit:junit:4.+")
    androidTestImplementation ("androidx.test.ext:junit:1.1.2")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.3.0")
}
