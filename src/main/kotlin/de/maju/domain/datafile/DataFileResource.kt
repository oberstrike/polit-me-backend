package de.maju.domain.datafile

import com.maju.openapi.annotations.OASPath
import com.maju.openapi.annotations.OASResource
import com.maju.openapi.codegen.RequestMethod
import com.maju.openapi.codegen.generators.annotations.schema.OASBaseSchemaEnum
import de.maju.domain.file.VideoFileService
import de.maju.domain.question.QuestionResource
import de.maju.securitySchemeName
import io.quarkus.panache.common.Sort
import java.io.File
import javax.ws.rs.BadRequestException
import javax.ws.rs.PathParam
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

const val dataFilePath = "/api/datafile"

@OASResource(path = dataFilePath, tagName = "DataFile", security = securitySchemeName)
class DataFileResource(
    private val dataFileService: DataFileService
) : IDataFileResource {

    @OASPath(path = "/id/{id}")
    override fun getDataFileById(@PathParam("id") id: Long): DataFileDTO {
        return dataFileService.findById(id)
    }

    @OASPath(
        produces = MediaType.APPLICATION_OCTET_STREAM,
        returnTypeSchema = OASBaseSchemaEnum.BINARY_STRING,
        requestMethod = RequestMethod.GET,
        path = "/id/{id}/file"
    )
    override fun getVideoFileByDataFileId(
        @PathParam("id") id: Long
    ): Response {
        val file =
            dataFileService.getContentOfFileById(id) ?: throw BadRequestException("There was an error while saving..")

        val responseBuilder = Response.ok(file)

        responseBuilder.header("Content-Disposition", "attachment;filename=" + file.name)
        return responseBuilder.build()
    }


    @OASPath
    override fun getDataFilesByQuery(
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
