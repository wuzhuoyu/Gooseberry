
plugins {
    kotlin("jvm")
    `maven-publish`
}

dependencies {
    implementation(kotlin("stdlib"))
}

sourceSets.main {
    java.srcDirs("src/main/kotlin")
}


afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("gooseberry-annotation-x5") {
                groupId = "com.yuu.android.component"
                artifactId = "gooseberry-annotation-x5"
                version = "0.0.1"
                from(components["java"])
            }
        }
    }
}
