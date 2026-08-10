plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
//    alias(libs.plugins.kover)
}

kotlin {
    android {
        namespace = "cmm.apps.esmorga"
        compileSdk = 37
        minSdk = 29
        withHostTestBuilder {}.configure {
            isIncludeAndroidResources = true
        }
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":domain"))
            implementation(project(":data"))
            implementation(project(":viewmodel"))
            implementation(project(":datasource-remote"))
            implementation(project(":datasource-local"))
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
        getByName("androidHostTest").dependencies {
            implementation(libs.core.ktx)
            implementation(libs.mockk)
            implementation(libs.robolectric)
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.test)
            implementation(libs.koin.android)
            implementation(libs.kotlin.datetime)

            //Mock server calls
            implementation(libs.ktor.test)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
        }
        androidMain.dependencies {
            implementation(libs.koin.android)
        }
    }
}
