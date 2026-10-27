package cmm.esmorga.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cmm.esmorga.App
import cmm.esmorga.domain.push.PushNotificationManager
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val pushNotificationManager: PushNotificationManager by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        pushNotificationManager.requestPermission()

        setContent {
            App()
        }
    }
}
