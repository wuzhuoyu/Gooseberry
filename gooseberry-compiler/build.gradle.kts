plugins {
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib"))
    api("com.google.devtools.ksp:symbol-processing-api:1.5.31-1.0.0")
    api("com.squareup:kotlinpoet:1.10.2")
    api(project(":gooseberry-annotation"))
}

