package cmm.esmorga.di

import cmm.esmorga.domain.push.PushNotificationManager
import cmm.esmorga.push.AndroidPushNotificationManager
import org.koin.core.module.Module
import org.koin.dsl.module

actual val pushPlatformModule: Module = module {
    single<PushNotificationManager> { AndroidPushNotificationManager(get()) }
}
