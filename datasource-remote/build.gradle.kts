plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.jetbrains.kotlin.serialization)
//    alias(libs.plugins.kover)
}

kotlin {
    android {
        namespace = "cmm.esmorga.datasource.remote"
        compileSdk = 37
        minSdk = 29
        withHostTest {
            isIncludeAndroidResources = true
        }
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "datasource-remote"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":data"))
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.kotlin.datetime)

        }
        commonTest.dependencies {
            implementation(libs.kotlinx.coroutines.test)
            implementation(libs.ktor.test)
        }
        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        androidMain.dependencies {
            implementation(libs.ktor.client.android)
        }
        getByName("androidHostTest").dependencies {
            implementation(libs.junit)
            implementation(libs.mockk)
            implementation(libs.robolectric)
        }
    }
}
