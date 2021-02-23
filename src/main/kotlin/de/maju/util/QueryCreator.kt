package de.maju.util

import javax.inject.Singleton


@Singleton
class QueryCreator() {

    fun createQuery(params: Map<String, Any>): String? {
        if (params.isEmpty()) return null

        return params.keys.joinToString(" and ") {
            val value = params[it]
            if (value is Number || value is Boolean) "$it = :$it"
            else "$it like concat(:$it, '%')"
        }
    }

}
