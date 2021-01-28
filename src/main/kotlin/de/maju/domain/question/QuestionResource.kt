package de.maju.domain.question

import com.maju.openapi.annotations.OASParameter
import com.maju.openapi.annotations.OASPath
import com.maju.openapi.annotations.OASResource
import com.maju.openapi.annotations.OASSchema
import com.maju.openapi.codegen.RequestMethod
import com.maju.openapi.codegen.generators.annotations.schema.OASBaseSchemaEnum
import de.maju.domain.comments.CommentDTO
import de.maju.domain.data.DataFile
import de.maju.domain.data.DataFileDTO
import io.quarkus.oidc.runtime.OidcJwtCallerPrincipal
import io.quarkus.security.Authenticated
import io.quarkus.security.identity.SecurityIdentity
import org.apache.commons.io.IOUtils
import org.eclipse.microprofile.openapi.models.parameters.RequestBody
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput
import java.io.InputStream
import javax.ws.rs.BadRequestException
import javax.ws.rs.DefaultValue
import javax.ws.rs.PathParam
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.MultivaluedMap
import javax.ws.rs.core.Response


const val questionPath = "/api/questions"


@OASResource(path = questionPath, tagName = "Question")
class QuestionResource(
    private val questionService: QuestionService,
    private val securityIdentity: SecurityIdentity
) : IQuestionResource {


    @OASPath(requestMethod = RequestMethod.DELETE, path = "/id/{id}", consumes = MediaType.TEXT_PLAIN)
    override fun deleteQuestionById(@PathParam("id") id: Long) {
        questionService.deleteById(id)
    }

    @OASPath
    override fun findQuestionByQuery(
        @QueryParam("page") @DefaultValue("0") page: Int,
        @QueryParam("pageSize") pageSize: Int
    ): List<QuestionDTO> {
        return questionService.findByQuery(page, pageSize)
    }


    @OASPath(produces = MediaType.APPLICATION_OCTET_STREAM,
        returnTypeSchema = OASBaseSchemaEnum.BINARY_STRING)
    override fun fileDownload(
        @PathParam("id") id: Long
    ): Response {
        return Response.ok().build()
    }


    @OASPath(
        requestMethod = RequestMethod.POST,
        consumes = MediaType.MULTIPART_FORM_DATA,
        path = "/id/{id}/file"
    )
    override fun fileUpload(
        @PathParam("id") id: Long,
        @MultipartForm @OASParameter(baseSchema = OASBaseSchemaEnum.FILE) multipartBody: MultipartFormDataInput
    ): Response {
        val uploadForm = multipartBody.formDataMap

        val content = uploadForm["content"]

        content?.get(0)?.let {
            val headers = it.headers
            val filename = getFileName(headers) ?: throw BadRequestException("The file has no name")
            val fileExtension = filename.substringAfter('.', "")

            val input = it.getBody(InputStream::class.java, null)
            val byteArray = IOUtils.toByteArray(input)

            val dataFile = DataFileDTO(
                content = byteArray!!.contentToString(),
                name = filename,
                extension = fileExtension
            )

            questionService.addDataFileToQuestionById(id, dataFile)
        }
        return Response.ok().build()
    }

    private fun getFileName(headers: MultivaluedMap<String, String>): String? {
        return headers["Content-Disposition"]?.let { contentDisposition ->
            return@let contentDisposition
                .first()
                .split(';')
                .map { it.trim() }
                .findLast {
                    it.startsWith("filename")
                }?.split("=")?.get(1)?.trim()?.replace("\"", "")
        }
    }


    @OASPath(requestMethod = RequestMethod.PUT)
    override fun updateQuestion(questionDTO: QuestionDTO): QuestionDTO {
        return questionService.update(questionDTO)
    }

    @OASPath(path = "/id/{id}", isProtected = false)
    override fun getQuestionById(@PathParam("id") id: Long): QuestionDTO {
        return questionService.findById(id)
    }

    @OASPath(path = "/id/{id}/comments", requestMethod = RequestMethod.POST, isProtected = true)
    @Authenticated
    override fun addCommentToQuestionById(@PathParam("id") id: Long, commentDTO: CommentDTO): QuestionDTO {
        val userId = (securityIdentity.principal as OidcJwtCallerPrincipal).claims.getClaimValueAsString("sub")
        return questionService.addCommentToQuestionById(id, commentDTO, userId)
    }

}
