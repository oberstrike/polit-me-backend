package de.maju.question

import de.maju.util.IMapper
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(uses=[QuestionAfterMapper::class],  componentModel = "cdi")
interface QuestionMapper : IMapper<Question, QuestionDTO> {

    @Mapping(target = "subject", ignore = true)
    override fun convertDTOToModel(dto: QuestionDTO): Question

    @Mapping(target = "subject", ignore = true)
    override fun convertModelToDTO(model: Question): QuestionDTO

}

