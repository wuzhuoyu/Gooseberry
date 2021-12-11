
plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
}

sourceSets.main {
    java.srcDirs("src/main/kotlin")
}
