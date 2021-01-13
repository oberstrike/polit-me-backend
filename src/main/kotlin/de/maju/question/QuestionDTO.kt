package de.maju.question

import com.maju.openapi.annotations.OASProperty
import com.maju.openapi.annotations.OASSchema
import com.maju.openapi.codegen.generators.annotations.schema.OASBaseSchemaEnum
import de.maju.comments.CommentDTO
import de.maju.subject.SubjectDTO
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
