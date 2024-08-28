package cmm.apps.data

import kotlinx.datetime.Clock


object CacheHelper {

    const val DEFAULT_CACHE_TTL = 30 * 60 * 1000 // 30 mins

    fun shouldReturnCache(dataTime: Long) = dataTime > Clock.System.now().toEpochMilliseconds() - DEFAULT_CACHE_TTL
}