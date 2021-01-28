package de.maju.util

class DataSize private constructor() {
    private val KB_FACTOR: Long = 1024
    private val MB_FACTOR = 1024 * KB_FACTOR
    private val GB_FACTOR = 1024 * MB_FACTOR

    var bytes: Double = 0.0

    private fun parseInternal(target: String): Double {
        val spaceNdx = target.indexOf(" ")
        val ret = target.substring(0, spaceNdx).toDouble()
        when (target.substring(spaceNdx + 1)) {
            "GB" -> return ret * GB_FACTOR
            "MB" -> return ret * MB_FACTOR
            "KB" -> return ret * KB_FACTOR
        }
        return (-1).toDouble()
    }

    companion object {
        fun parse(target: String): DataSize {
            return DataSize().apply { bytes = parseInternal(target) }
        }
    }
}


