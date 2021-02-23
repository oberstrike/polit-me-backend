package de.maju.domain.subject

import com.maju.utils.IConverter
import de.maju.domain.question.QuestionMapper
import de.maju.util.LocalDateTimeMapper
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper

@Mapper(
    uses = [QuestionMapper::class, SubjectAfterMapper::class, LocalDateTimeMapper::class],
    componentModel = "cdi",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
interface SubjectMapper : IConverter<Subject, SubjectDTO>
