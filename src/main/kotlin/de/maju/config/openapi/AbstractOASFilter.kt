package de.maju.config.openapi

import org.eclipse.microprofile.openapi.OASFilter
import org.eclipse.microprofile.openapi.models.OpenAPI
import org.eclipse.microprofile.openapi.models.media.Schema
import org.eclipse.microprofile.openapi.models.servers.Server

abstract class AbstractOASFilter : OASFilter, IFilterSchema {

    interface IOnFilterSchemaHandler {
        fun onFilterSchema(schema: Schema)
    }

    interface IOnFilterOpenApiHandler {
        fun onFilterOpenApi(openAPI: OpenAPI)
    }

    override val onFilterSchemaHandlers: List<IOnFilterSchemaHandler> = emptyList()

    override val onFilterOpenApiHandlers: List<IOnFilterOpenApiHandler> = emptyList()

    override fun filterSchema(schema: Schema): Schema {
        onFilterSchemaHandlers.onEach { it.onFilterSchema(schema) }
        return super.filterSchema(schema)
    }

    override fun filterOpenAPI(openAPI: OpenAPI) {
        onFilterOpenApiHandlers.onEach { it.onFilterOpenApi(openAPI) }
        super.filterOpenAPI(openAPI)
    }


    override fun filterServer(server: Server?): Server {
        return super.filterServer(server)
    }
}

interface IFilterSchema {
    val onFilterSchemaHandlers: List<AbstractOASFilter.IOnFilterSchemaHandler>
    val onFilterOpenApiHandlers: List<AbstractOASFilter.IOnFilterOpenApiHandler>

}


