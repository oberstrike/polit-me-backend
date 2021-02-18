package de.maju.util

import io.quarkus.panache.common.Sort


object Direction {

    fun ofAbbreviation(abbreviation: String): Sort.Direction {
        return if (abbreviation == "desc") Sort.Direction.Descending else Sort.Direction.Ascending
    }
}
