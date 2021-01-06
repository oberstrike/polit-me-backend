package de.maju.subject

import com.maju.annotations.InjectionStrategy
import com.maju.annotations.RepositoryProxy
import de.maju.question.Question
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import javax.enterprise.context.ApplicationScoped
import javax.persistence.Entity
import javax.persistence.OneToMany

@Entity
data class Subject(
    var content: String = "",
    var headline: String = "",
    @OneToMany(mappedBy = "subject")
    var questions: MutableList<Question> = mutableListOf()
) : PanacheEntity()


@ApplicationScoped
@RepositoryProxy(
    converter = SubjectMapper::class
)
class SubjectRepository : PanacheRepository<Subject> {
    fun save(subject: Subject): Subject {
        persist(subject)
        flush()
        return subject
    }

    fun getAll(): List<Subject> {
        return findAll().list()
    }

}
