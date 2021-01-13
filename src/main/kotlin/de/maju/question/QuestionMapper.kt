package de.maju.question

import com.maju.utils.IConverter
import de.maju.comments.CommentMapper
import de.maju.util.IMapper
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper(uses = [QuestionAfterMapper::class, CommentMapper::class], componentModel = "cdi", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
abstract class QuestionMapper : IConverter<Question, QuestionDTO> {

    @Mappings(
        Mapping(target = "subject", ignore = true),
    )
    abstract override fun convertDTOToModel(dto: QuestionDTO): Question

    @Mappings(
        Mapping(target = "subject", ignore = true),
    )
    abstract override fun convertModelToDTO(model: Question): QuestionDTO

}

