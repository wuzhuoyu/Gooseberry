
//ksp
pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}


dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "Gooseberry"
include (":app")
include (":gooseberry-api")
include (":gooseberry-complier")
include (":gooseberry-annotation")
