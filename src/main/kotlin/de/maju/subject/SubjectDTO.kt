package de.maju.subject

import com.fasterxml.jackson.annotation.JsonInclude
import com.maju.openapi.annotations.OASSchema
import de.maju.question.QuestionDTO
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType
import org.eclipse.microprofile.openapi.annotations.media.Schema

@OASSchema("Subject")
@Schema(name = "Subject")
data class SubjectDTO(
    val id: Long? = null,
    val content: String,
    var headline: String = "",
    val questions: List<QuestionDTO> = listOf()
)
