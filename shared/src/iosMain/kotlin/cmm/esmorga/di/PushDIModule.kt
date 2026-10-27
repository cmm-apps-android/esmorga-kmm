package cmm.esmorga.di

import cmm.esmorga.domain.push.PushNotificationManager
import cmm.esmorga.push.IosPushNotificationManager
import org.koin.core.module.Module
import org.koin.dsl.module

val iosPushNotificationManager = IosPushNotificationManager()

actual val pushPlatformModule: Module = module {
    single<PushNotificationManager> { iosPushNotificationManager }
}
