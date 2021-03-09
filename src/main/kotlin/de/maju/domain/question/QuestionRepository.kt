package de.maju.domain.question

import com.maju.annotations.RepositoryProxy
import de.maju.domain.datafile.DataFileRepository
import de.maju.util.Direction
import de.maju.util.QueryCreator
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import io.quarkus.panache.common.Page
import io.quarkus.panache.common.Sort
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
@RepositoryProxy(converters = [QuestionMapper::class])
class QuestionRepository : PanacheRepository<Question> {

    @Inject
    lateinit var dataFileRepository: DataFileRepository

    @Inject
    lateinit var questionPanacheRepository: QuestionPanacheParamCreator

    fun findByQuery(
        page: Int,
        pageSize: Int,
        sort: String,
        dir: String,
        questionBeanParam: QuestionBeanParam
    ): List<Question> {
        val params = questionPanacheRepository.createParams(questionBeanParam)
        val query = QueryCreator.createQuery(params)

        val sortObject = Sort.by(sort, Direction.ofAbbreviation(dir))
        val pageObject = Page.of(page, pageSize)

        if (query != null)
            return find(query, sortObject, params).page(pageObject).list()
        return findAll(sortObject).page(pageObject).list()
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
