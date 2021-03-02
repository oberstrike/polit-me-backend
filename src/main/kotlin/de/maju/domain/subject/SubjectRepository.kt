package de.maju.domain.subject

import com.maju.annotations.RepositoryProxy
import de.maju.util.Direction
import de.maju.util.PagedRequest
import de.maju.util.QueryCreator
import de.maju.util.SortedRequest
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import io.quarkus.panache.common.Page
import io.quarkus.panache.common.Sort
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
@RepositoryProxy(
    converter = SubjectMapper::class
)
class SubjectRepository(
    private val subjectPanacheParamCreator: SubjectPanacheParamCreator
) : PanacheRepository<Subject> {

    fun findByQuery(
        sortedRequest: SortedRequest,
        pagedRequest: PagedRequest,
        subjectBeanParam: SubjectBeanParam
    ): List<Subject> {
        val sorting = Sort.by(sortedRequest.sort, Direction.ofAbbreviation(sortedRequest.direction))
        val paging = Page.of(pagedRequest.page, pagedRequest.pageSize)
        val params = subjectPanacheParamCreator.createParams(subjectBeanParam)

        if (params.isNotEmpty()) {
            val query = QueryCreator.createQuery(params)
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
