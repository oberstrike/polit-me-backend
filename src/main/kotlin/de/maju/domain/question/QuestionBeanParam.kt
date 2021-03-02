package de.maju.domain.question

import de.maju.domain.comments.CommentDTO
import javax.ws.rs.QueryParam

class QuestionBeanParam {
    @QueryParam("owner")
    val owner: String? = null

    @QueryParam("id")
    val id: Long? = null

    @QueryParam("isPublic")
    val public: Boolean? = null

    @QueryParam("created")
    val created: Long? = null
}
