package de.maju.domain.question

import com.maju.openapi.annotations.OASParameter
import com.maju.openapi.codegen.generators.annotations.schema.OASBaseSchemaEnum
import de.maju.domain.comments.CommentDTO
import de.maju.domain.datafile.DataFileDTO
import de.maju.util.PagedRequest
import de.maju.util.SortedRequest
import io.quarkus.oidc.runtime.OidcJwtCallerPrincipal
import io.quarkus.security.Authenticated
import io.quarkus.security.identity.SecurityIdentity
import org.apache.commons.io.IOUtils
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import org.eclipse.microprofile.openapi.annotations.tags.Tags
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput
import java.io.InputStream
import javax.ws.rs.*
import javax.ws.rs.core.MultivaluedMap
import javax.ws.rs.core.Response


const val questionPath = "/api/questions"


@Path(value = "/api/questions")
@Tags(Tag(name = "Question", description = ""))
class QuestionResource(
    private val questionService: QuestionService,
    private val securityIdentity: SecurityIdentity
) {


    @DELETE
    @Produces(value = ["text/plain"])
    @Consumes(value = ["text/plain"])
    @Path(value = "/id/{id}")
    fun deleteQuestionById(@PathParam("id") id: Long) {
        questionService.deleteById(id)
    }

    @GET
    @Produces(value = ["application/json"])
    fun findQuestionByQuery(
        @BeanParam sortedRequest: SortedRequest,
        @BeanParam pagedRequest: PagedRequest,
        @BeanParam questionBeanParam: QuestionBeanParam
    ): List<QuestionDTO> {
        return questionService.findByQuery(
            pagedRequest.page,
            pagedRequest.pageSize,
            sortedRequest.sort,
            sortedRequest.direction,
            questionBeanParam
        )
    }


    @POST
    @Produces(value = ["application/json"])
    @Consumes(value = ["multipart/form-data"])
    @Path(value = "/id/{id}/file")
    fun setFileToQuestionById(
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
            val contentAsByteArray = IOUtils.toByteArray(input)

            val dataFile = DataFileDTO(
                name = filename,
                extension = fileExtension
            )

            questionService.setDataFileToQuestionById(id, dataFile, contentAsByteArray)
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


    @PUT
    fun updateQuestion(questionDTO: QuestionDTO): QuestionDTO {
        return questionService.update(questionDTO)
    }

    @GET
    @Produces(value = ["application/json"])
    @Path(value = "/id/{id}")
    fun getQuestionById(@PathParam("id") id: Long): QuestionDTO {
        return questionService.findById(id)
    }

    @POST
    @Produces(value = ["application/json"])
    @Consumes(value = ["application/json"])
    @Path(value = "/id/{id}/comments")
    @Authenticated
    fun addCommentToQuestionById(@PathParam("id") id: Long, commentDTO: CommentDTO): QuestionDTO {
        val userId =
            (securityIdentity.principal as OidcJwtCallerPrincipal).claims.getClaimValueAsString("sub")
        return questionService.addCommentToQuestionById(id, commentDTO, userId)
    }

}
