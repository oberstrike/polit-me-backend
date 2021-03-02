package de.maju.domain.subject

import de.maju.domain.question.Question
import de.maju.domain.question.QuestionDTO
import de.maju.domain.question.QuestionRepository
import de.maju.util.toEpochMilli
import de.maju.util.toZonedDateTime
import org.mapstruct.AfterMapping
import org.mapstruct.MappingTarget
import org.mapstruct.ObjectFactory
import org.mapstruct.TargetType
import java.time.*
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class SubjectAfterMapper {

    @Inject
    lateinit var questionRepository: QuestionRepository

    @AfterMapping
    fun mapDTOToModel(dto: SubjectDTO, @MappingTarget subject: Subject) {
        subject.questions.clear()
        val questionIds = dto.questions.map { it.id }
        for (questionId in questionIds) {
            if (questionId != null) {
                val question = questionRepository.findById(questionId) ?: continue
                subject.questions.add(question)
            }
        }
    }

}
