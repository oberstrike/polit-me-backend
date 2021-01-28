package de.maju.domain.data

import com.maju.utils.IConverter
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper(
    uses = [DataFileAfterMapper::class],
    componentModel = "cdi",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
interface DataFileMapper : IConverter<DataFile, DataFileDTO> {

    @Mappings(
        Mapping(target = "content", ignore = true),
    )
    override fun convertDTOToModel(dto: DataFileDTO): DataFile

    @Mappings(
        Mapping(target = "content", ignore = true),
    )
    override fun convertModelToDTO(model: DataFile): DataFileDTO
}
