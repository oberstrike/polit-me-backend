package de.maju.config

import io.smallrye.openapi.api.models.media.SchemaImpl
import org.eclipse.microprofile.openapi.OASFilter
import org.eclipse.microprofile.openapi.models.media.Schema


class CustomOnFilterSchemaHandler : AbstractOASFilter.IOnFilterSchemaHandler {
    private val baseStringPattern: String = "^[a-zA-Z0-9 ]*\$"
    private val baseMinLength: String = "0"
    private val baseMaxLength: String = "100"

    override fun onFilterSchema(schema: Schema) {
        val isArray = schema.type == Schema.SchemaType.ARRAY
        if(isArray){
            println(schema)
        }

        when (schema.type) {
            Schema.SchemaType.OBJECT -> schema.additionalPropertiesBoolean = false
            Schema.SchemaType.ARRAY -> if (schema.maxItems == null) {
                schema.maxItems = 100
            }

            else -> Unit
        }
    }
}

class CustomOASFilter : AbstractOASFilter() {
    override val onFilterSchemaHandlers: List<IOnFilterSchemaHandler> = listOf(CustomOnFilterSchemaHandler())
}
