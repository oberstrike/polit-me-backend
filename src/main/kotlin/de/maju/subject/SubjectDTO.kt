package de.maju.subject

import com.maju.openapi.annotations.OASSchema
import de.maju.question.QuestionDTO
import org.eclipse.microprofile.openapi.annotations.media.Schema

@OASSchema("Subject")
@Schema(name = "Subject")
data class SubjectDTO(
    val content: String,
    val id: Long? = null,
    var headline: String = "",
    val questions: MutableList<QuestionDTO> = mutableListOf(),
    val deleted: Boolean = false
)
