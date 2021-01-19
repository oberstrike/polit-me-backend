package de.maju.domain.subject

import de.maju.domain.question.QuestionRepository
import de.maju.util.toEpochMilli
import de.maju.util.toZonedDateTime
import org.mapstruct.AfterMapping
import org.mapstruct.MappingTarget
import java.time.*
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class SubjectAfterMapper {

    @Inject
    lateinit var questionRepository: QuestionRepository

    @AfterMapping
    fun mapDTO(dto: SubjectDTO, @MappingTarget subject: Subject) {
        val created = dto.created
        subject.created = if (created == 0L) LocalDateTime.now() else created.toZonedDateTime()

        subject.questions.clear()
        val questionIds = dto.questions.map { it.id }
        for (questionId in questionIds) {
            if (questionId != null) {
                val question = questionRepository.findById(questionId) ?: continue
                subject.questions.add(question)
            }
        }
    }

    @AfterMapping
    fun mapModel(@MappingTarget dto: SubjectDTO, subject: Subject) {
        val created = subject.created
        dto.created = created.toEpochMilli()
    }
}
