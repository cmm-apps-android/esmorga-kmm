enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Esmorga"
include(":androidApp")
include(":shared")
include(":domain")
include(":data")
include(":viewmodel")
include(":datasource-remote")
include(":datasource-local")
include(":android-design-system")
