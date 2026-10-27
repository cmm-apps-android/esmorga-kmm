import SwiftUI
import shared
import OneSignalFramework

class AppDelegate: NSObject, UIApplicationDelegate, IosPushNotificationDelegate, OSNotificationClickListener {
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        OneSignal.initialize("0115e97b-297b-4eec-bd1a-130fe1e6a960", withLaunchOptions: launchOptions)

        let versionName = Bundle.main.infoDictionary?["CFBundleShortVersionString"] as? String ?? "1.0"
        #if DEBUG
        let environment = "qa"
        #else
        let environment = "prod"
        #endif

        OneSignal.User.addTags([
            "environment": environment,
            "platform": "ios",
            "version": versionName
        ])

        OneSignal.Notifications.addClickListener(self)

        OneSignal.Notifications.requestPermission({ accepted in
            print("User accepted notifications: \(accepted)")
            if accepted {
                DispatchQueue.main.async {
                    application.registerForRemoteNotifications()
                }
            }
        }, fallbackToSettings: true)

        PushDIModuleKt.iosPushNotificationManager.setDelegate(delegate: self)

        return true
    }

    func onClick(event: OSNotificationClickEvent) {
        let additionalData = event.notification.additionalData
        let type = additionalData?["type"] as? String
        PushDIModuleKt.iosPushNotificationManager.onNotificationClicked(type: type)
    }

    func initialize(appId: String) {
        OneSignal.initialize(appId)
    }

    func requestPermission(onResult: @escaping (KotlinBoolean) -> Void) {
        OneSignal.Notifications.requestPermission({ accepted in
            onResult(KotlinBoolean(bool: accepted))
            if accepted {
                DispatchQueue.main.async {
                    UIApplication.shared.registerForRemoteNotifications()
                }
            }
        }, fallbackToSettings: true)
    }

    func login(userId: String) {
        OneSignal.login(userId)
    }

    func logout() {
        OneSignal.logout()
    }

    func addTags(tags: [String : String]) {
        OneSignal.User.addTags(tags)
    }
}

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) var appDelegate

    init() {
        KoinKt.doInitKoin()
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
                .ignoresSafeArea()
        }
    }
}
