package de.maju.config.openapi

import io.smallrye.openapi.api.models.ComponentsImpl
import io.smallrye.openapi.api.models.OpenAPIImpl
import io.smallrye.openapi.api.models.media.SchemaImpl
import org.eclipse.microprofile.openapi.OASModelReader
import org.eclipse.microprofile.openapi.models.OpenAPI

class ModelReader : OASModelReader {

    override fun buildModel(): OpenAPI {
        val api = OpenAPIImpl()
        val schema = SchemaImpl("Array2")

        val components = ComponentsImpl()
        components.addSchema("Array2", schema)

        api.components(components)
        return api
    }
}
