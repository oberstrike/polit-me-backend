package de.maju.domain.datafile

import de.maju.domain.file.VideoFileRepository
import org.mapstruct.AfterMapping
import org.mapstruct.MappingTarget
import java.util.*
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class DataFileAfterMapper(
    private val videoFileRepository: VideoFileRepository
) {

    @AfterMapping
    fun mapDTO(dto: DataFileDTO, @MappingTarget dataFile: DataFile) {
        dataFile.videoFile = dto.id?.let { videoFileRepository.findById(it) }
    }

    @AfterMapping
    fun mapModel(@MappingTarget dto: DataFileDTO, dataFile: DataFile) {
        dto.videoFile = dataFile.videoFile?.id
    }
}
