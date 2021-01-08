package de.maju.question

import com.maju.annotations.RepositoryProxy
import de.maju.subject.Subject
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import javax.enterprise.context.ApplicationScoped
import javax.persistence.*

@Entity
class Question(
    var owner: String = ""
) : PanacheEntity() {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subject_id")
    var subject: Subject? = null

    @Lob
    var content: ByteArray = ByteArray(0)
}

@ApplicationScoped
@RepositoryProxy(converter = QuestionMapper::class)
class QuestionRepository : PanacheRepository<Question> {

    fun save(question: Question): Question {
        persist(question)
        flush()
        return question
    }

    fun update(questionToUpdate: Question): Question? {
        if (questionToUpdate.id == null) return null
        val question = findById(questionToUpdate.id!!) ?: return null
        question.subject = questionToUpdate.subject
        question.owner = questionToUpdate.owner
        question.content = question.content
        return save(question)
    }

}
