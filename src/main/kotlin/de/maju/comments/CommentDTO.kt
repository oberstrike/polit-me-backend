package de.maju.comments

import com.maju.openapi.annotations.OASSchema
import de.maju.question.QuestionDTO
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType
import org.eclipse.microprofile.openapi.annotations.media.Schema

@OASSchema("Comment")
@Schema(name = "Comment")
data class CommentDTO(
    override var id: Long? = null,
    override var question: Long? = null,
    override var isDeleted: Boolean = false,
    override var createDateTime: Long = 0,
    override var content: String = ""
) : ICommentDTO

