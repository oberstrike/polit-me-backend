package de.maju.domain.subject

import com.maju.annotations.RepositoryProxy
import de.maju.util.Direction
import de.maju.util.ParamCreator
import de.maju.util.QueryCreator
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import io.quarkus.panache.common.Page
import io.quarkus.panache.common.Sort
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
@RepositoryProxy(
    converter = SubjectMapper::class
)
class SubjectRepository(
    private val subjectPanacheParamCreator: SubjectPanacheParamCreator,
    private val paramCreator: QueryCreator
) : PanacheRepository<Subject> {

    fun findByQuery(
        sort: String,
        direction: String,
        page: Int,
        pageSize: Int,
        subjectBeanParam: SubjectBeanParam
    ): List<Subject> {
        val sorting = Sort.by(sort, Direction.ofAbbreviation(direction))
        val paging = Page.of(page, pageSize)
        val params = subjectPanacheParamCreator.createParams(subjectBeanParam)

        if (params.isNotEmpty()) {
            val query = paramCreator.createQuery(params)
            if (query != null) {
                return find(query, sorting, params).page(paging).list()
            }
        }
        return findAll(sorting).page(paging).list()
    }

    fun save(subject: Subject): Subject {
        val old = subject.id?.let { findById(it) }

        if (old != null) {
            old.isDeleted = subject.isDeleted
            old.headline = subject.headline
            old.content = subject.content
            for (question in subject.questions) {
                old.questions.add(question)
            }

            persist(old)
            flush()
            return old
        }
        persist(subject)
        flush()
        return subject
    }

    fun getAll(): List<Subject> {
        return findAll().list()
    }

    fun purge(subject: Subject) {
        subject.questions.clear()
        delete(subject)
    }

    fun update(subject: Subject): Subject? {
        if (subject.id == null) return null
        val oldSubject = findById(subject.id!!) ?: return null

        oldSubject.content = subject.content
        oldSubject.created = subject.created
        oldSubject.headline = subject.headline
        oldSubject.isDeleted = subject.isDeleted
        oldSubject.isPublic = subject.isPublic

        oldSubject.questions.clear()
        for (question in subject.questions) {
            oldSubject.questions.add(question)
        }

        persist(oldSubject)
        flush()
        return oldSubject
    }

    fun purgeAll() {
        findAll().stream().forEach {
            purge(it)
        }
    }
}
