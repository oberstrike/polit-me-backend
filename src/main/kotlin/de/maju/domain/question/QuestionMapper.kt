package de.maju.domain.question

import com.maju.utils.IConverter
import de.maju.domain.comments.CommentMapper
import de.maju.domain.datafile.DataFileMapper
import de.maju.util.LocalDateTimeMapper
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper(
    uses = [
        QuestionAfterMapper::class,
        LocalDateTimeMapper::class,
        CommentMapper::class,
        DataFileMapper::class
    ],
    componentModel = "cdi",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
abstract class QuestionMapper : IConverter<Question, QuestionDTO> {

    @Mappings(
        Mapping(target = "subject", ignore = true),
        Mapping(target = "dataFile", ignore = true),
    )
    abstract override fun convertDTOToModel(dto: QuestionDTO): Question

    @Mappings(
        Mapping(target = "subject", ignore = true),
        Mapping(target = "dataFile", ignore = true),

        )
    abstract override fun convertModelToDTO(model: Question): QuestionDTO

}

