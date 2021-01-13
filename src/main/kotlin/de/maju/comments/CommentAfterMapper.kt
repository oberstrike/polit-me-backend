package de.maju.comments

import de.maju.question.QuestionRepository
import de.maju.util.toEpochMilli
import de.maju.util.toLocalDateTime
import de.maju.util.toZonedDateTime
import org.mapstruct.AfterMapping
import org.mapstruct.MappingTarget
import java.time.LocalDateTime
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class CommentAfterMapper {

    @Inject
    lateinit var questionRepository: QuestionRepository

    @AfterMapping
    fun mapDTO(commentDTO: CommentDTO, @MappingTarget comment: Comment) {

        //Date
        val createDateTime = commentDTO.createDateTime
        comment.createDateTime = if (createDateTime == 0L) LocalDateTime.now() else createDateTime.toZonedDateTime()

        //Question
        val question = commentDTO.question ?: return
        comment.question = questionRepository.findById(question)

    }

    @AfterMapping
    fun mapModel(@MappingTarget commentDTO: CommentDTO, comment: Comment) {
        //Date
        val createDateTime = comment.createDateTime
        commentDTO.createDateTime = createDateTime.toEpochMilli()

        //Question
        val question = comment.question ?: return
        commentDTO.question = question.id
    }

}
