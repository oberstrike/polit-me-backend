package de.maju.question

import de.maju.subject.SubjectRepository
import org.mapstruct.AfterMapping
import org.mapstruct.MappingTarget
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class QuestionAfterMapper {

    @Inject
    lateinit var subjectRepository: SubjectRepository

    @AfterMapping
    fun setSubject(dto: QuestionDTO, @MappingTarget question: Question) {
        val subjectId = dto.subject ?: return

        val subject = subjectRepository.findById(subjectId)
        if (subject != null) {
            question.subject = subject
        }
    }

    @AfterMapping
    fun setSubjectId(@MappingTarget dto: QuestionDTO, question: Question) {
        val subjectId = question.subject?.id
        if (subjectId != null) {
            dto.id = subjectId
        }
    }

}
