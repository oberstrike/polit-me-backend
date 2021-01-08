package de.maju.util

import de.maju.question.QuestionMapper
import de.maju.question.QuestionRepository
import de.maju.question.QuestionRepositoryProxy
import de.maju.subject.SubjectMapper
import de.maju.subject.SubjectRepository
import de.maju.subject.SubjectRepositoryProxy
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.inject.Produces

@ApplicationScoped
class Config {

    @Produces
    fun subjectRepositoryProxy(converter: SubjectMapper, subjectRepository: SubjectRepository) =
        SubjectRepositoryProxy(converter, subjectRepository)


    @Produces
    fun questionRepositoryProxy(converter: QuestionMapper, questionRepository: QuestionRepository) =
        QuestionRepositoryProxy(converter, questionRepository)

}
