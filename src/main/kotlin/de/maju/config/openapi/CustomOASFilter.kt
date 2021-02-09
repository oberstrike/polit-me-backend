package de.maju.config.openapi

import org.eclipse.microprofile.openapi.models.media.Schema


class CustomOnFilterSchemaHandler : AbstractOASFilter.IOnFilterSchemaHandler {

    override fun onFilterSchema(schema: Schema) {
        when (schema.type) {
            Schema.SchemaType.OBJECT -> {
                schema.additionalPropertiesBoolean = false

            }
            Schema.SchemaType.ARRAY -> if (schema.maxItems == null) {
                schema.maxItems = 100
            }
            else -> Unit
        }
    }
}

class CustomOASFilter : AbstractOASFilter() {
    override val onFilterSchemaHandlers: List<IOnFilterSchemaHandler> = listOf(CustomOnFilterSchemaHandler())
    override val onFilterOpenApiHandlers: List<IOnFilterOpenApiHandler> = listOf()
}
