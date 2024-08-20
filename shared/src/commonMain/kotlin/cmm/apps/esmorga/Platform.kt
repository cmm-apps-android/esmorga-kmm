package cmm.apps.esmorga

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform