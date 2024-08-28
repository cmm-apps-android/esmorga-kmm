package cmm.apps.esmorga.android

import android.app.Application
import androidx.lifecycle.LifecycleObserver
import cmm.apps.esmorga.ViewDIModule
import cmm.apps.esmorga.di.sharedKoinModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class EsmorgaApp : Application(), LifecycleObserver {

    companion object {
        private var mInstance: EsmorgaApp? = null

        fun instance(): EsmorgaApp? {
            return mInstance
        }
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {
            allowOverride(false)
            androidLogger()
            androidContext(this@EsmorgaApp)
            modules(ViewDIModule.modules + sharedKoinModules)
        }
    }

}