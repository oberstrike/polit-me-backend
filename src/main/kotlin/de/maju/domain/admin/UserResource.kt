package de.maju.domain.admin

import org.eclipse.microprofile.openapi.annotations.enums.SchemaType
import org.eclipse.microprofile.openapi.annotations.media.Content
import org.eclipse.microprofile.openapi.annotations.media.Schema
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/api/user")
class UserResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses(
        APIResponse(
            responseCode = "200", description = "The request was successful.", content =
            arrayOf(
                Content(
                    schema = Schema(
                        implementation = KeycloakUserDTO::class, type =
                        SchemaType.ARRAY, maxItems = 100
                    ), mediaType = "application/json"
                )
            )
        )
    )
    fun getUsers(): List<KeycloakUserDTO> {
        return emptyList()
    }
}
