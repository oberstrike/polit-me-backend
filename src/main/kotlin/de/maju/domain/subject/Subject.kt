package de.maju.domain.subject

import com.maju.annotations.RepositoryProxy
import de.maju.domain.question.Question
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import java.time.LocalDateTime
import javax.enterprise.context.ApplicationScoped
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.OneToMany


@Entity
data class Subject(
    var created: LocalDateTime = LocalDateTime.now(),
    @OneToMany(
        mappedBy = "subject",
        cascade = [CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE],
        orphanRemoval = true
    )
    var questions: MutableList<Question> = mutableListOf(),
    var content: String = "",
    var headline: String = "",
    var isDeleted: Boolean = false,
    var isPublic: Boolean = false
) : PanacheEntity() {


    fun addQuestion(question: Question) {
        question.subject = this
        questions.add(question)
    }
}


@ApplicationScoped
@RepositoryProxy(
    converter = SubjectMapper::class
)
class SubjectRepository : PanacheRepository<Subject> {
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
