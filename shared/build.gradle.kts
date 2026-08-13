plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.compose.compiler)
//    alias(libs.plugins.kover)
}

kotlin {
    android {
        namespace = "cmm.esmorga"
        compileSdk = 37
        minSdk = 29
        withHostTest {
            isIncludeAndroidResources = true
        }
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
        }
        experimentalProperties["android.experimental.kmp.enableAndroidResources"] = true
    }
    
    listOf(
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
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(libs.androidx.navigation.compose)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(compose.components.resources)
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.coil3.compose)
            implementation(libs.coil3.network.ktor)
            implementation(project(":domain"))
            implementation(project(":data"))
            implementation(project(":viewmodel"))
            implementation(project(":datasource-remote"))
            implementation(project(":datasource-local"))
            implementation(project(":design-system"))
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

compose.resources {
    publicResClass = true
}
