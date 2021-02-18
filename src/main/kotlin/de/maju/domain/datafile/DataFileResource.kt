package de.maju.domain.datafile

import io.quarkus.panache.common.Sort
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType
import org.eclipse.microprofile.openapi.annotations.media.Content
import org.eclipse.microprofile.openapi.annotations.media.Schema
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import org.eclipse.microprofile.openapi.annotations.tags.Tags
import javax.ws.rs.*
import javax.ws.rs.core.Response

const val dataFilePath = "/api/datafile"

@Path(value = "/api/datafile")
@Tags(Tag(name = "DataFile", description = ""))
class DataFileResource(
    private val dataFileService: DataFileService
) {

    @GET
    @Produces(value = ["application/json"])
    @Path(value = "/id/{id}")
    fun getDataFileById(@PathParam("id") id: Long): DataFileDTO {
        return dataFileService.findById(id)
    }

    @GET
    @Produces(value = ["application/octet-stream"])
    @Path(value = "/id/{id}/file")
    @APIResponses(
        APIResponse(
            responseCode = "200", description = "The request was successful.", content =
            arrayOf(
                Content(
                    schema = Schema(
                        type = SchemaType.STRING, maxLength = 144, minLength =
                        0, pattern = "^[a-zA-Z0-9 ]*$", format = "binary"
                    ), mediaType =
                    "application/octet-stream"
                )
            )
        )
    )
    fun getVideoFileByDataFileId(
        @PathParam("id") id: Long
    ): Response {
        val file =
            dataFileService.getContentOfFileById(id) ?: throw BadRequestException("There was an error while saving..")

        val responseBuilder = Response.ok(file)

        responseBuilder.header("Content-Disposition", "attachment;filename=" + file.name)
        return responseBuilder.build()
    }


    @GET
    @Produces(value = ["application/json"])
    fun getDataFilesByQuery(
        @QueryParam("sort") sort: String?,
        @QueryParam("direction") dir: String?,
        @QueryParam("page") page: Int?,
        @QueryParam("pageSize") pageSize: Int?
    ): List<DataFileDTO> {
        return dataFileService.findByQuery(
            sort ?: "id",
            dir ?: Sort.Direction.Ascending.toString(),
            0,
            10
        )
    }

}
