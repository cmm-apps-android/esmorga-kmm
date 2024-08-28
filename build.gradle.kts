plugins {
    alias(libs.plugins.androidApplication).apply(false)
    alias(libs.plugins.androidLibrary).apply(false)
    alias(libs.plugins.kotlinAndroid).apply(false)
    alias(libs.plugins.kotlinMultiplatform).apply(false)
    alias(libs.plugins.kotlin.parcelize) apply false
//    alias(libs.plugins.kover)
}

//dependencies {
//    kover(project(":androidApp"))
//    kover(project(":viewmodel"))
//    kover(project(":domain"))
//    kover(project(":data"))
//    kover(project(":datasource-remote"))
//    kover(project(":datasource-local"))
//    kover(project(":shared"))
//}

//kover {
//    // Execute all tests with ./gradlew koverHtmlReport
//    reports {
//        // filters for all report types of all build variants
//        filters {
//            excludes {
//                androidGeneratedClasses()
//            }
//        }
//    }
//}