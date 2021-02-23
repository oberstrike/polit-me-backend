package de.maju.util

interface ParamCreator<T> {
    fun createParams(target: T): Map<String, Any>
}
