package de.maju.question

import com.maju.openapi.annotations.OASProperty
import com.maju.openapi.annotations.OASSchema
import com.maju.openapi.codegen.generators.annotations.schema.OASBaseSchemaEnum
import de.maju.subject.SubjectDTO
import org.eclipse.microprofile.openapi.annotations.media.Schema

@OASSchema("Question")
@Schema(name = "QuestionDTO")
data class QuestionDTO(
    override val owner: String,
    override val subject: Long? = null,
    override var id: Long? = null,
    @OASProperty(baseSchema = OASBaseSchemaEnum.FILE)
    override val content: ByteArray
) : IQuestionDTO
