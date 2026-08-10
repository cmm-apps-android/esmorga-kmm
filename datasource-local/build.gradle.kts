plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.devtools.ksp)
    alias(libs.plugins.room)
//    alias(libs.plugins.kover)
}

kotlin {
    android {
        namespace = "cmm.apps.datasource.local"
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
            baseName = "datasource-local"
            isStatic = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.core.ktx)
            implementation(libs.room.ktx)
        }
        commonMain.dependencies {
            api(project(":data"))
            implementation(libs.sqlite.bundled)
            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.koin.core)
            implementation(libs.kotlinx.coroutines)
            implementation(libs.room.runtime)
            implementation(libs.kotlin.datetime)
        }
        commonTest.dependencies {
            implementation(libs.kotlinx.coroutines.test)
        }
        getByName("androidHostTest").dependencies {
            implementation(libs.junit)
            implementation(libs.mockk)
            implementation(libs.robolectric)
        }
    }
}

dependencies {
    add("kspAndroid", libs.room.compiler)
    add("kspIosSimulatorArm64", libs.room.compiler)
    add("kspIosX64", libs.room.compiler)
    add("kspIosArm64", libs.room.compiler)
}
room {
    schemaDirectory("$projectDir/schemas")
}
