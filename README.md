# Esmorga KMP Project

This is a Kotlin Multiplatform (KMP) project providing shared logic and UI components for Android and iOS.

## 📚 KMP Integration Guide: Android & iOS

This section summarizes how to consume these KMP modules in native Android and iOS applications based on recent research and configuration.

### 🤖 Android Integration (Composite Builds)

For native Android projects, we use **Gradle Composite Builds**. This allows simultaneous development on the KMP library and the Android app without publishing to Maven.

#### 1. Include the Project
In the native Android project's `settings.gradle.kts`:
```kotlin
includeBuild("../EsmorgaKMP")
```
*Note: Gradle automatically substitutes dependencies if the group and module names match.*

#### 2. Implementation
In the app-level `build.gradle.kts`:
```kotlin
dependencies {
    implementation("cmm.esmorga:shared")
}
```

---

### 🍎 iOS Integration (Xcode)

The iOS integration uses the `embedAndSign` task to link the Kotlin code as a Framework.

#### 1. Build Phase Run Script
In your Xcode project, add a **New Run Script Phase** *before* the "Compile Sources" phase:
```bash
cd "$SRCROOT/../EsmorgaKMP"
./gradlew :shared:embedAndSignAppleFrameworkForXcode
```

#### 2. Framework Configuration
*   **Linking:** Manually add the `shared.framework` (found in the KMP project's `build/xcode-frameworks` folder) to the "Frameworks, Libraries, and Embedded Content" section in Xcode.
*   **Static Frameworks:** The project is configured with `isStatic = true` to simplify linking and avoid common signing issues.

#### 3. Xcode 15+ Sandbox Fix
To bypass build script sandboxing issues, the following configuration is included in the `:shared` module:
```kotlin
tasks.matching { it.name == "checkSandboxAndWriteProtection" }.configureEach {
    enabled = false
}
```

---

### 🛠 Transitive Dependencies & Visibility

To expose code from internal modules (like `:data` or `:design-system`) to the native side, explicit exporting is required.

#### `implementation` vs `api`
*   **`implementation`**: The dependency is hidden from consumers. Swift will not see these classes.
*   **`api`**: The dependency is exported. Required for any module whose classes need to be accessed from native code.

#### Exporting for iOS
In `shared/build.gradle.kts`:
```kotlin
kotlin {
    listOf(iosArm64(), iosSimulatorArm64()).forEach {
        it.binaries.framework {
            baseName = "shared"
            isStatic = true
            // Export modules to make them visible in the Framework header (.h)
            export(project(":data"))
            export(project(":design-system"))
        }
    }
    sourceSets {
        commonMain.dependencies {
            // Must use 'api' to be able to 'export' the project
            api(project(":data"))
            api(project(":design-system"))
            
            // Internal-only modules can remain as implementation
            implementation(project(":domain"))
        }
    }
}
```

---

### 💡 Tips & Best Practices
*   **Clean Build:** If changes are not reflected in Swift, run `./gradlew clean` in the KMP project.
*   **Native UI:** The `:shared` module provides `UIViewController` wrappers to embed Compose Multiplatform views into SwiftUI.
*   **Dependency Injection:** Initialize Koin in the native `AppDelegate` (iOS) or `Application` (Android) using the `initKoin` helper.
