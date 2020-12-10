package de.maju.subject

import com.maju.openapi.annotations.OASSchema
import de.maju.question.QuestionDTO
import javax.validation.constraints.NotBlank

@OASSchema("SubjectDTO")
data class SubjectDTO(
    override val id: Long? = null,
    @NotBlank(message = "The content of the subject may not be blank")
    override val content: String,
    override val questions: List<QuestionDTO>
): ISubjectDTO
