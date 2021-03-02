package de.maju.domain.subject

import de.maju.domain.admin.KeycloakUser
import de.maju.domain.question.Question
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.OneToMany


@Entity
data class Subject(
    var headline: String = "",
    var content: String = "",
    var created: LocalDateTime = LocalDateTime.now(),
    @OneToMany(
        mappedBy = "subject",
        cascade = [CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE],
        orphanRemoval = true
    )
    var questions: MutableList<Question> = mutableListOf(),
    var isDeleted: Boolean = false,
    var isPublic: Boolean = false,
    val owner: KeycloakUser? = null
) : PanacheEntity()
