package de.maju.domain.subject

import com.maju.openapi.annotations.OASSchema
import de.maju.domain.question.QuestionDTO
import org.eclipse.microprofile.openapi.annotations.media.Schema

@OASSchema("Subject")
@Schema(name = "Subject")
data class SubjectDTO(
    override val content: String,
    override val id: Long? = null,
    override var headline: String = "",
    override val questions: MutableList<QuestionDTO> = mutableListOf(),
    override val deleted: Boolean = false,
    override var isPublic: Boolean = false,
    override var created: Long = 0
) : ISubjectDTO, SubjectCreateDTO

interface SubjectCreateDTO {
    val content: String
}
