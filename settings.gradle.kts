
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
        jcenter() // Warning: this repository is going to shut down soon
    }
}
rootProject.name = "Gooseberry"
include (":app")
include (":gooseberry-api-x5")
include (":gooseberry-compiler-x5")
include (":gooseberry-annotation-x5")
