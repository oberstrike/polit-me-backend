package de.maju.subject

import com.maju.annotations.RepositoryProxy
import de.maju.question.Question
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import javax.enterprise.context.ApplicationScoped
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.OneToMany


@Entity
data class Subject(
    @OneToMany(
        mappedBy = "subject",
        cascade = [CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE],
        orphanRemoval = true
    )
    var questions: MutableList<Question> = mutableListOf(),
    var content: String = "",
    var headline: String = "",
    var isDeleted: Boolean = false
) : PanacheEntity()


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

}
