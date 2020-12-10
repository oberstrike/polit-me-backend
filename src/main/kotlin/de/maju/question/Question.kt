package de.maju.question

import de.maju.subject.Subject
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import javax.enterprise.context.ApplicationScoped
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.Lob
import javax.persistence.ManyToOne

@Entity
data class Question(
    @Lob
    var content: ByteArray = ByteArray(0),
    @ManyToOne
    @JoinColumn(name = "SUBJECT_ID")
    var subject: Subject = Subject()
) : PanacheEntity()

@ApplicationScoped
class QuestionRepository : PanacheRepository<Question>
