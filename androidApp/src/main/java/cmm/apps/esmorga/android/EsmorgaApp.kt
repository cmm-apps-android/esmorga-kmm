package cmm.apps.esmorga.android

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import cmm.apps.esmorga.di.initKoin
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class EsmorgaApp : Application(), LifecycleObserver {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidLogger()
            androidContext(this@EsmorgaApp)
        }
    }
}