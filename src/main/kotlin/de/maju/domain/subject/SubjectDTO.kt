package de.maju.domain.subject

import com.maju.openapi.annotations.OASSchema
import de.maju.domain.question.QuestionDTO
import org.eclipse.microprofile.openapi.annotations.media.Schema
import org.jboss.resteasy.annotations.Query
import javax.enterprise.context.ApplicationScoped


@OASSchema("Subject")
@Schema(name = "Subject")
data class SubjectDTO(
    override val content: String,
    override val id: Long? = null,
    override var headline: String? = null,
    override var questions: MutableList<QuestionDTO> = mutableListOf(),
    override val deleted: Boolean = false,
    override var isPublic: Boolean = false,
    override var created: Long = 0
) : ISubjectDTO

@OASSchema("SubjectCreate")
@Schema(name =" SubjectCreate")
data class SubjectCreateDTO(
    override val content: String,
    override val headline: String
): ISubjectCreateDTO
