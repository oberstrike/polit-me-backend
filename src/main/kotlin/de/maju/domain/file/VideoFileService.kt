package de.maju.domain.file

import de.maju.domain.datafile.FileSystemRepository
import java.io.File
import javax.enterprise.context.ApplicationScoped
import javax.transaction.Transactional
import javax.ws.rs.NotFoundException

@ApplicationScoped
class VideoFileService(
    private val videoFileRepository: VideoFileRepository,
    private val fileSystemRepository: FileSystemRepository
) {

    fun findByContentName(name: String): VideoFile? {
        return videoFileRepository.find("fileName", name).firstResult()
    }

    fun findById(id: Long): VideoFile? {
        return videoFileRepository.findById(id)
    }

    @Transactional
    fun save(videoFile: VideoFile) {
        videoFileRepository.persist(videoFile)
    }

    @Transactional
    fun deleteById(id: Long): Boolean {
        val videoFile =
            videoFileRepository.findById(id) ?: throw NotFoundException("There was no VideoFile with he ID $id found.")
        val fileName = videoFile.fileName
        fileSystemRepository.delete(fileName)
        return videoFileRepository.deleteById(id)
    }

    fun getContentOfFile(fileName: String): File? {
        return fileSystemRepository.findByName(fileName)
    }

}
