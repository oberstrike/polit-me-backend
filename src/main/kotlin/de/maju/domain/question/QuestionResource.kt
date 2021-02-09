package de.maju.domain.question

import com.maju.openapi.annotations.OASParameter
import com.maju.openapi.annotations.OASPath
import com.maju.openapi.annotations.OASResource
import com.maju.openapi.codegen.RequestMethod
import com.maju.openapi.codegen.generators.annotations.schema.OASBaseSchemaEnum
import de.maju.domain.comments.CommentDTO
import de.maju.domain.datafile.DataFileDTO
import io.quarkus.oidc.runtime.OidcJwtCallerPrincipal
import io.quarkus.security.Authenticated
import io.quarkus.security.identity.SecurityIdentity
import org.apache.commons.io.IOUtils
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput
import java.io.InputStream
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.MultivaluedMap
import javax.ws.rs.core.Response


const val questionPath = "/api/questions"


@OASResource(path = questionPath, tagName = "Question", security = "openIdConnect")
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



    @OASPath(
        requestMethod = RequestMethod.POST,
        consumes = MediaType.MULTIPART_FORM_DATA,
        path = "/id/{id}/file",
        returnTypeSchema = OASBaseSchemaEnum.STRING_WITH_SPACES
    )
    override fun addFileToQuestionById(
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
                name = filename,
                extension = fileExtension
            )

            questionService.setDataFileToQuestionById(id, dataFile, byteArray)
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

    @OASPath(path = "/id/{id}", security = "")
    override fun getQuestionById(@PathParam("id") id: Long): QuestionDTO {
        return questionService.findById(id)
    }

    @OASPath(path = "/id/{id}/comments", requestMethod = RequestMethod.POST)
    @Authenticated
    override fun addCommentToQuestionById(@PathParam("id") id: Long, commentDTO: CommentDTO): QuestionDTO {
        val userId = (securityIdentity.principal as OidcJwtCallerPrincipal).claims.getClaimValueAsString("sub")
        return questionService.addCommentToQuestionById(id, commentDTO, userId)
    }

}
