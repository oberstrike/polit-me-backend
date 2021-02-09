package de.maju.domain.subject

import com.maju.annotations.RepositoryProxy
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import io.quarkus.panache.common.Page
import io.quarkus.panache.common.Sort
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
@RepositoryProxy(
    converter = SubjectMapper::class
)
class SubjectRepository : PanacheRepository<Subject> {

    fun findByQuery(sort: String, direction: String, page: Int, pageSize: Int): List<Subject> {
        return findAll(Sort.by(sort, Sort.Direction.valueOf(direction))).page(Page.of(page, pageSize)).list()
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
