package de.maju.question

import com.maju.openapi.annotations.OASProperty
import com.maju.openapi.annotations.OASSchema
import com.maju.openapi.codegen.generators.annotations.schema.OASBaseSchemaEnum
import de.maju.subject.SubjectDTO
import org.eclipse.microprofile.openapi.annotations.media.Schema

@OASSchema("Question")
@Schema(name = "Question")
data class QuestionDTO(
    override var id: Long,
    @OASProperty(baseSchema = OASBaseSchemaEnum.FILE)
    override val content: ByteArray,
    override val subject: Long
): IQuestionDTO
