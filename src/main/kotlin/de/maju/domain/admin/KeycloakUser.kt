package de.maju.domain.admin

import de.maju.domain.comments.Comment
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import org.eclipse.microprofile.openapi.annotations.media.Schema
import javax.enterprise.context.ApplicationScoped
import javax.persistence.*

@Entity
data class KeycloakUser(
    @OneToMany(
        mappedBy = "keycloakUser",
        cascade = [CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE],
        orphanRemoval = true
    )
    var comments: MutableList<Comment> = mutableListOf(),
    var userId: String = ""
) : PanacheEntity()


@Schema
data class KeycloakUserDTO(
    val id: Long,
    val userId: String
)

@ApplicationScoped
class KeycloakUserRepository : PanacheRepository<KeycloakUser>
