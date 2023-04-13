
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
            create<MavenPublication>("gooseberry-annotation") {
                groupId = "com.yuu.android.component"
                artifactId = "gooseberry-annotation"
                version = "0.0.1"
                from(components["java"])
            }
        }
    }
}
