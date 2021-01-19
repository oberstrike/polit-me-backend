package de.maju.domain.comments

import com.maju.utils.IConverter
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper(uses = [CommentAfterMapper::class],  componentModel = "cdi", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
interface CommentMapper : IConverter<Comment, CommentDTO> {

    @Mappings(
        Mapping(target = "createDateTime", ignore = true),
        Mapping(target = "question", ignore = true)
    )
    override fun convertDTOToModel(dto: CommentDTO): Comment


    @Mappings(
        Mapping(target = "createDateTime", ignore = true),
        Mapping(target = "question", ignore = true)
    )    override fun convertModelToDTO(model: Comment): CommentDTO

}
