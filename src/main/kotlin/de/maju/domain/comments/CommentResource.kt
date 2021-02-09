package de.maju.domain.comments

import com.maju.openapi.annotations.OASPath
import com.maju.openapi.annotations.OASResource
import com.maju.openapi.codegen.RequestMethod
import javax.enterprise.context.ApplicationScoped
import javax.ws.rs.BadRequestException
import javax.ws.rs.PathParam
import javax.ws.rs.QueryParam

const val commentPath = "/api/comments"

@ApplicationScoped
@OASResource(path = commentPath, tagName = "Comments", security = "openIdConnect")
class CommentResource(
    private val commentService: CommentService
) : ICommentResource {

    @OASPath(requestMethod = RequestMethod.DELETE, path = "/id/{id}")
    override fun deleteCommentById(@PathParam("id") id: Long) {
        val isDeleted = commentService.deleteById(id)
        if (!isDeleted) throw BadRequestException("There was an error while deleting $id")
    }

    @OASPath(path = "/id/{id}")
    override fun findCommentById(@PathParam("id") id: Long): CommentDTO {
        return commentService.findById(id)
    }

    @OASPath(requestMethod = RequestMethod.PUT)
    override fun updateComment(commentDTO: CommentDTO): CommentDTO {
        return commentService.update(commentDTO)
    }

    @OASPath
    override fun findCommentByQuery(@QueryParam("page") page: Int, @QueryParam("pageSize") pageSize: Int): List<CommentDTO> {
        return commentService.findAll(page, pageSize)
    }

}
