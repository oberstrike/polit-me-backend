package de.maju.domain.question

import com.maju.annotations.RepositoryProxy
import de.maju.domain.datafile.DataFileRepository
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import io.quarkus.panache.common.Page
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
@RepositoryProxy(converter = QuestionMapper::class)
class QuestionRepository : PanacheRepository<Question> {

    @Inject
    lateinit var dataFileRepository: DataFileRepository

    fun findByQuery(page: Int, pageSize: Int): List<Question> {
        return findAll().page(Page.of(page, pageSize)).list()
    }

    fun save(question: Question): Question {
        persist(question)
        question.dataFile?.let { dataFileRepository.persist(it) }
        flush()
        return question
    }

    fun update(questionToUpdate: Question): Question? {
        if (questionToUpdate.id == null) return null
        val question = findById(questionToUpdate.id!!) ?: return null
        question.subject = questionToUpdate.subject
        question.owner = questionToUpdate.owner
        question.dataFile = questionToUpdate.dataFile
        question.comments.clear()
        question.comments.addAll(questionToUpdate.comments)
        return save(question)
    }

    fun findBySubjectId(id: Long, page: Int, pageSize: Int): List<Question> {
        return find("Subject.id", id).page(Page.of(page, pageSize)).list()
    }

    fun purge(question: Question) {
        question.comments.clear()
        delete(question)
    }

    fun purgeAll() {
        findAll().stream().forEach {
            purge(it)
        }
    }

}
