package de.maju.subject

import de.maju.question.Question
import de.maju.question.QuestionDTO
import de.maju.util.toEpochMilli
import de.maju.util.toLocalDateTime
import de.maju.util.toZonedDateTime
import org.mapstruct.AfterMapping
import org.mapstruct.MappingTarget
import java.time.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class SubjectAfterMapper {

    @AfterMapping
    fun setModelCreated(dto: SubjectDTO, @MappingTarget subject: Subject) {
        val created = dto.created
        subject.created = if (created == 0L) LocalDateTime.now() else created.toZonedDateTime()
    }

    @AfterMapping
    fun setDTOCreated(@MappingTarget dto: SubjectDTO, subject: Subject) {
        val created = subject.created

        dto.created = created.toEpochMilli()
    }
}
