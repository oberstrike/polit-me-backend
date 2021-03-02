package de.maju.util


object QueryCreator {

    fun createQuery(params: Map<String, Any>): String? {
        if (params.isEmpty()) return null

        return params.keys.joinToString(" and ") {
            val value = params[it]
            if(value is String) "$it like concat(:$it, '%')"
            else if (value is Number || value is Boolean) "$it = :$it"
            else "$it = :$it"
        }
    }

}
