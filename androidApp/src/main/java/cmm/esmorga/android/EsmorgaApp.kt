package cmm.esmorga.android

import android.app.Application
import android.content.pm.ApplicationInfo
import androidx.lifecycle.LifecycleObserver
import cmm.esmorga.di.initKoin
import cmm.esmorga.domain.push.PushNotificationManager
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class EsmorgaApp : Application(), LifecycleObserver {

    private val pushNotificationManager: PushNotificationManager by inject()

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger()
            androidContext(this@EsmorgaApp)
        }

        pushNotificationManager.initialize(ONESIGNAL_APP_ID)

        val versionName = try {
            packageManager.getPackageInfo(packageName, 0).versionName ?: "1.0"
        } catch (_: Exception) {
            "1.0"
        }
        val isDebug = (applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
        val environment = if (isDebug) "qa" else "prod"

        pushNotificationManager.addTags(
            mapOf(
                "environment" to environment,
                "platform" to "android",
                "version" to versionName
            )
        )
    }

    companion object {
        const val ONESIGNAL_APP_ID = "0115e97b-297b-4eec-bd1a-130fe1e6a960"
    }
}
