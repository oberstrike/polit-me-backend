package de.maju.domain.data

import org.mapstruct.AfterMapping
import org.mapstruct.MappingTarget
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class DataFileAfterMapper {

    @AfterMapping
    fun mapDTO(dto: DataFileDTO, @MappingTarget dataFile: DataFile) {
        val content = Base64.getEncoder().encode(dto.content?.toByteArray())
        dataFile.content = content
    }

    @AfterMapping
    fun mapModel(@MappingTarget dto: DataFileDTO, dataFile: DataFile) {
        val content = Base64.getEncoder().encodeToString(dataFile.content)
        dto.content = content
    }
}
