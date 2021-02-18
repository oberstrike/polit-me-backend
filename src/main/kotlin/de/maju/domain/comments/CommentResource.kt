package de.maju.domain.comments

import org.eclipse.microprofile.openapi.annotations.tags.Tag
import org.eclipse.microprofile.openapi.annotations.tags.Tags
import javax.ws.rs.*

const val commentPath = "/api/comments"

@Path(value = "/api/comments")
@Tags(Tag(name = "Comments", description = ""))
class CommentResource(
    private val commentService: CommentService
) {

    @DELETE
    @Produces(value = ["text/plain"])
    @Consumes(value = ["application/json"])
    @Path(value = "/id/{id}")
    fun deleteCommentById(@PathParam("id") id: Long) {
        val isDeleted = commentService.deleteById(id)
        if (!isDeleted) throw BadRequestException("There was an error while deleting $id")
    }

    @GET
    @Produces(value = ["application/json"])
    @Path(value = "/id/{id}")
    fun findCommentById(@PathParam("id") id: Long): CommentDTO {
        return commentService.findById(id)
    }

    @PUT
    @Produces(value = ["application/json"])
    @Consumes(value = ["application/json"])
    fun updateComment(commentDTO: CommentDTO): CommentDTO {
        return commentService.update(commentDTO)
    }

    @GET
    @Produces(value = ["application/json"])
    fun findCommentByQuery(@QueryParam("page") page: Int, @QueryParam("pageSize") pageSize: Int): List<CommentDTO> {
        return commentService.findAll(page, pageSize)
    }

}
