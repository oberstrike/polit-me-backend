package de.maju.util

import de.maju.domain.comments.CommentMapper
import de.maju.domain.comments.CommentRepository
import de.maju.domain.comments.CommentRepositoryProxy
import de.maju.domain.datafile.DataFileMapper
import de.maju.domain.datafile.DataFileRepository
import de.maju.domain.datafile.DataFileRepositoryProxy
import de.maju.domain.question.QuestionMapper
import de.maju.domain.question.QuestionRepository
import de.maju.domain.question.QuestionRepositoryProxy
import de.maju.domain.subject.*
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

    @Produces
    fun commentRepositoryProxy(converter: CommentMapper, commentRepository: CommentRepository) =
        CommentRepositoryProxy(converter, commentRepository)


    @Produces
    fun dataFileRepositoryProxy(converter: DataFileMapper, dataFileRepository: DataFileRepository) =
        DataFileRepositoryProxy(converter, dataFileRepository)



}
