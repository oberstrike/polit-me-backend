package de.maju.domain.question

import com.maju.openapi.annotations.OASProperty
import com.maju.openapi.annotations.OASSchema
import com.maju.openapi.codegen.generators.annotations.schema.OASBaseSchemaEnum
import de.maju.domain.comments.CommentDTO
import org.eclipse.microprofile.openapi.annotations.media.Schema

@OASSchema("Question")
@Schema(name = "Question")
data class QuestionDTO(
    override val owner: String,
    @OASProperty(baseSchema = OASBaseSchemaEnum.FILE)
    override val content: ByteArray,
    override var subject: Long? = null,
    override var id: Long? = null,
    override var isPublic: Boolean = false,
    override var comments: MutableList<CommentDTO> = mutableListOf()
) : IQuestionDTO
