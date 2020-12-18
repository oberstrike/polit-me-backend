package de.maju.config

import io.smallrye.openapi.api.models.ComponentsImpl
import io.smallrye.openapi.api.models.OpenAPIImpl
import io.smallrye.openapi.api.models.media.SchemaImpl
import io.smallrye.openapi.api.models.servers.ServerImpl
import io.smallrye.openapi.api.models.servers.ServerVariableImpl
import org.eclipse.microprofile.openapi.OASFilter
import org.eclipse.microprofile.openapi.OASModelReader
import org.eclipse.microprofile.openapi.models.OpenAPI
import org.eclipse.microprofile.openapi.models.media.Schema
import org.eclipse.microprofile.openapi.models.servers.Server

abstract class AbstractOASFilter : OASFilter, IFilterSchema {

    interface IOnFilterSchemaHandler {
        fun onFilterSchema(schema: Schema)
    }


    override val onFilterSchemaHandlers: List<IOnFilterSchemaHandler> = emptyList()

    override fun filterSchema(schema: Schema): Schema {
        onFilterSchemaHandlers.onEach { it.onFilterSchema(schema) }
        return super.filterSchema(schema)
    }

    override fun filterOpenAPI(openAPI: OpenAPI?) {
        super.filterOpenAPI(openAPI)
    }


    override fun filterServer(server: Server?): Server {
        return super.filterServer(server)
    }
}

interface IFilterSchema {
    val onFilterSchemaHandlers: List<AbstractOASFilter.IOnFilterSchemaHandler>
}

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
