package de.maju.comments

import com.maju.annotations.RepositoryProxy
import de.maju.question.Question
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import java.time.LocalDateTime
import javax.enterprise.context.ApplicationScoped
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
data class Comment(
    var userId: String = "",
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
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
class CommentRepository : PanacheRepository<Comment>{

    fun save(comment: Comment): Comment {
        persist(comment)
        flush()
        return comment
    }

}
