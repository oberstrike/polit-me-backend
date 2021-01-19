package de.maju.admin

import de.maju.domain.comments.Comment
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import javax.enterprise.context.ApplicationScoped
import javax.persistence.*

@Entity(name = "party")
data class User(
    @OneToMany(
        mappedBy = "user",
        cascade = [CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE],
        orphanRemoval = true
    )
    var comments: MutableList<Comment> = mutableListOf(),
    var userId: String = ""
) : PanacheEntity()

data class UserDTO(
    val id: Long,
    val userId: String
)

@ApplicationScoped
class UserRepository : PanacheRepository<User>
