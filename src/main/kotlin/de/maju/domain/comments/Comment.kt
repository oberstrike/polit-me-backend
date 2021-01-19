package de.maju.domain.comments

import com.maju.annotations.RepositoryProxy
import de.maju.admin.User
import de.maju.domain.question.Question
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import io.quarkus.panache.common.Page
import java.time.LocalDateTime
import javax.enterprise.context.ApplicationScoped
import javax.persistence.*

@Entity
data class Comment(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: User? = null,
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = [CascadeType.REFRESH, CascadeType.PERSIST])
    @JoinColumn(name = "question_id")
    var question: Question? = null,
    var isDeleted: Boolean = false,
    var createDateTime: LocalDateTime = LocalDateTime.now(),
    var content: String = ""
) : PanacheEntity()


@RepositoryProxy(
    converter = CommentMapper::class
)
@ApplicationScoped
class CommentRepository : PanacheRepository<Comment> {

    fun save(comment: Comment): Comment {
        persist(comment)
        flush()
        return comment
    }

    fun update(comment: Comment): Comment? {
        val old = findById(comment.id!!) ?: return null
        old.createDateTime = comment.createDateTime
        old.question = comment.question
        old.content = comment.content
        return old
    }

    fun findByQuery(page: Int, pageSize: Int): List<Comment> {
        return findAll().page(Page.of(page, pageSize)).list()
    }

    fun findByQuestionId(questionId: Long): Comment? {
        return find("question.id", questionId).firstResult()
    }

}
