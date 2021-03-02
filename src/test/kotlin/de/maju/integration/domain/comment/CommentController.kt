package de.maju.integration.domain.comment

import de.maju.domain.comments.CommentDTO
import de.maju.domain.comments.commentPath
import de.maju.integration.domain.question.fromJson
import de.maju.rest.util.Controller
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class CommentController {

    @Inject
    lateinit var controller: Controller

    fun deleteById(id: Long): Boolean {
        val response = controller.sendDelete("$commentPath/id/$id")
        val statusCode = response.statusCode
        if (statusCode != 204) {
            println("There was an error: ${response.body} (status: $statusCode)")
            return false
        }
        return true
    }

    fun findById(id: Long): CommentDTO? {
        val response = controller.sendGet("$commentPath/id/$id")
        val statusCode = response.statusCode
        if (statusCode != 200) return null
        return controller.fromJson(response.body.asString())
    }

    fun updateComment(commentDTO: CommentDTO): CommentDTO? {
        val response = controller.sendPut(commentPath, controller.toJson(commentDTO))
        val statusCode = response.statusCode
        if (statusCode != 200) return null
        return controller.fromJson(response.body.asString())

    }

    fun find(page: Int, pageSize: Int): List<CommentDTO>? {
        val response = controller.sendGet(
            commentPath, params = mapOf(
                "page" to page,
                "pageSize" to pageSize
            )
        )
        val statusCode = response.statusCode
        if (statusCode != 200) return null
        return controller.fromJson<Array<CommentDTO>>(response.body.asString()).toList()
    }

}
