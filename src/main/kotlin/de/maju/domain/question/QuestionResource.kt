package de.maju.domain.question

import com.maju.openapi.annotations.OASParameter
import com.maju.openapi.annotations.OASPath
import com.maju.openapi.annotations.OASResource
import com.maju.openapi.codegen.RequestMethod
import de.maju.domain.comments.CommentDTO
import io.quarkus.oidc.runtime.OidcJwtCallerPrincipal
import io.quarkus.security.Authenticated
import io.quarkus.security.identity.SecurityIdentity
import javax.ws.rs.DefaultValue
import javax.ws.rs.PathParam
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType

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
