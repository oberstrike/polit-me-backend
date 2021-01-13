package de.maju.subject

import com.maju.utils.IConverter
import de.maju.question.QuestionMapper
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(uses = [QuestionMapper::class, SubjectAfterMapper::class], componentModel = "cdi", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
interface SubjectMapper : IConverter<Subject, SubjectDTO> {

    @Mapping(target = "created", ignore = true)
    override fun convertModelToDTO(model: Subject): SubjectDTO

    @Mapping(target = "created", ignore = true)
    override fun convertDTOToModel(dto: SubjectDTO): Subject
}



