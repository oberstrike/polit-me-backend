package de.maju.domain.question

import de.maju.domain.comments.Comment
import de.maju.domain.datafile.DataFile
import de.maju.domain.subject.Subject
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import java.time.LocalDateTime
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

    @OneToOne(fetch = FetchType.LAZY,
        cascade = [CascadeType.REMOVE, CascadeType.REFRESH],
        orphanRemoval = true)
    var dataFile: DataFile? = null

    var created: LocalDateTime = LocalDateTime.now()
}

