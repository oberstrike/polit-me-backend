package de.maju.domain.question

import com.maju.annotations.RepositoryProxy
import de.maju.domain.comments.Comment
import de.maju.domain.subject.Subject
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import io.quarkus.panache.common.Page
import javax.enterprise.context.ApplicationScoped
import javax.persistence.*

@Entity
class Question(
    var owner: String = ""
) : PanacheEntity() {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "subject_id")
    var subject: Subject? = null

    @OneToMany(
        mappedBy = "question",
        cascade = [CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE],
        orphanRemoval = true
    )
    var comments: MutableList<Comment> = mutableListOf()

    var isPublic: Boolean = false


    @Lob
    @Column(name = "content", length = 1000000)
    var content: ByteArray = ByteArray(0)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Question) return false

        if (owner != other.owner) return false
        if (subject != other.subject) return false
        if (!content.contentEquals(other.content)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = owner.hashCode()
        result = 31 * result + (subject?.hashCode() ?: 0)
        result = 31 * result + content.contentHashCode()
        return result
    }




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
        question.content = questionToUpdate.content
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
