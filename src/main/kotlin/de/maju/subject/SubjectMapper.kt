package de.maju.subject

import com.maju.utils.IConverter
import de.maju.question.QuestionMapper
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper

@Mapper(uses = [QuestionMapper::class], componentModel = "cdi", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
interface SubjectMapper : IConverter<Subject, SubjectDTO> {

}



