package cmm.esmorga.viewmodel.common

fun String?.parseCoordinates(): Pair<Double, Double>? {
    if (this.isNullOrBlank()) return null
    val parts = this.split(",")
    if (parts.size != 2) return null
    val lat = parts[0].trim().toDoubleOrNull()
    val lng = parts[1].trim().toDoubleOrNull()
    if (lat == null || lng == null) return null
    return Pair(lat, lng)
}

fun String?.isValidCoordinates(): Boolean {
    if (this.isNullOrBlank()) return true
    return this.parseCoordinates() != null
}
