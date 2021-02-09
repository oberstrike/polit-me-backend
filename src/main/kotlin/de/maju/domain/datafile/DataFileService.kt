package de.maju.domain.datafile

import de.maju.domain.file.VideoFileService
import java.io.File
import javax.enterprise.context.ApplicationScoped
import javax.transaction.Transactional
import javax.ws.rs.BadRequestException
import javax.ws.rs.NotFoundException

@ApplicationScoped
class DataFileService(
    private val dataFileRepositoryProxy: DataFileRepositoryProxy,
    private val videoFileService: VideoFileService
) {

    @Transactional
    fun findByQuery(sort: String, direction: String, page: Int, pageSize: Int): List<DataFileDTO> {
        return dataFileRepositoryProxy.findByQuery(sort, direction, page, pageSize)
    }

    @Transactional
    fun findById(id: Long): DataFileDTO {
        return dataFileRepositoryProxy.findById(id)
            ?: throw NotFoundException("There was no dataFile with the ID: $id found")
    }

    @Transactional
    fun deleteById(id: Long): Boolean {
        val dataFileDTO =
            dataFileRepositoryProxy.findById(id) ?: throw NotFoundException("No DataFile with the ID: $id was found.")
        val videoFile = dataFileDTO.videoFile
        if (videoFile != null) {
            videoFileService.deleteById(videoFile)
        }

        return dataFileRepositoryProxy.deleteById(id)
    }

    @Transactional
    fun getContentOfFileById(id: Long): File? {
        val dataFile = dataFileRepositoryProxy.findById(id)
        val videoFileId = dataFile?.videoFile
        val videoFile = videoFileId?.let { videoFileService.findById(videoFileId) } ?: throw NotFoundException("")
        val file = videoFileService.getContentOfFile(videoFile.fileName) ?: throw BadRequestException("")
        val newFile = File(dataFile.name)
        newFile.writeBytes(file.readBytes())
        return newFile
    }

    fun save(dataFile: DataFileDTO): DataFileDTO? {
        return dataFileRepositoryProxy.save(dataFile)
    }


}
