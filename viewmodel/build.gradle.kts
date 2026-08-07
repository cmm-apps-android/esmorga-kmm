plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
//    alias(libs.plugins.kover)
}

kotlin {
    android {
        namespace = "cmm.esmorga.viewmodel"
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
            baseName = "viewmodel"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":domain"))
            implementation(libs.kotlinx.coroutines)
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.kotlin.datetime)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        getByName("androidHostTest").dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.junit)
            implementation(libs.mockk)
            implementation(libs.robolectric)
        }
        androidMain.dependencies {
            implementation(libs.androidx.lifecycle.viewmodel)
        }
    }
}
