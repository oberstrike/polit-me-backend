package de.maju.domain.question

import com.maju.openapi.annotations.OASSchema
import de.maju.domain.comments.CommentDTO
import org.eclipse.microprofile.openapi.annotations.media.Schema

@OASSchema("Question")
@Schema(name = "Question")
data class QuestionDTO(
    override val owner: String,
    override var dataFile: Long? = null,
    override var subject: Long? = null,
    override var id: Long? = null,
    override var isPublic: Boolean = false,
    override var comments: MutableList<CommentDTO> = mutableListOf(),
    override var created: Long = 0
) : IQuestionDTO

