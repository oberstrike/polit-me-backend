package de.maju.domain.comments

import com.maju.annotations.RepositoryProxy
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import io.quarkus.panache.common.Page
import javax.enterprise.context.ApplicationScoped

@RepositoryProxy(
    converters = [CommentMapper::class]
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
