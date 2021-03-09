package de.maju.util

data class PagedResponse<T>(
    val content: List<T>,
    val page: Int,
    val pageCount: Int
)
