package de.maju.util

interface Validator<T> {

    fun validate(target: T): Boolean
}
