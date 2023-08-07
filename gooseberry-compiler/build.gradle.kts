plugins {
    kotlin("jvm")
    `maven-publish`
}

dependencies {
    implementation(kotlin("stdlib"))
    api("com.google.devtools.ksp:symbol-processing-api:1.5.31-1.0.0")
    api("com.squareup:kotlinpoet:1.10.2")
    api(project(":gooseberry-annotation"))
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("gooseberry-compiler") {
                groupId = "com.yuu.android.component"
                artifactId = "gooseberry-compiler"
                version = "0.0.1"
                from(components["java"])
            }
        }
    }
}

