package de.maju.domain.comments

import de.maju.domain.admin.KeycloakUser
import de.maju.domain.question.Question
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import java.time.LocalDateTime
import javax.persistence.*

@Entity
data class Comment(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keycloakUser_id")
    var keycloakUser: KeycloakUser? = null,

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = [CascadeType.REFRESH, CascadeType.PERSIST])
    @JoinColumn(name = "question_id")
    var question: Question? = null,
    var isDeleted: Boolean = false,
    var createDateTime: LocalDateTime = LocalDateTime.now(),
    var content: String = ""
) : PanacheEntity()


