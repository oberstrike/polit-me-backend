package de.maju.question

import com.maju.utils.IConverter
import de.maju.util.IMapper
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(uses=[QuestionAfterMapper::class],  componentModel = "cdi", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
interface QuestionMapper :IConverter<Question, QuestionDTO> {

    @Mapping(target = "subject", ignore = true)
    override fun convertDTOToModel(dto: QuestionDTO): Question

    @Mapping(target = "subject", ignore = true)
    override fun convertModelToDTO(model: Question): QuestionDTO

}

