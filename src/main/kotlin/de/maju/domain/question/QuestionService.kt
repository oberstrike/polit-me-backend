package de.maju.domain.question

import de.maju.domain.admin.KeycloakUserRepository
import de.maju.domain.comments.CommentDTO
import de.maju.domain.comments.CommentRepository
import de.maju.domain.comments.CommentRepositoryProxy
import de.maju.domain.datafile.*
import de.maju.domain.file.VideoFile
import de.maju.domain.file.VideoFileService
import de.maju.util.Validator
import java.util.*
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs.BadRequestException
import javax.ws.rs.NotFoundException

@ApplicationScoped
class QuestionService {

    @Inject
    lateinit var userRepository: KeycloakUserRepository

    @Inject
    lateinit var questionRepositoryProxy: QuestionRepositoryProxy

    @Inject
    lateinit var questionRepository: QuestionRepository

    @Inject
    lateinit var commentRepositoryProxy: CommentRepositoryProxy

    @Inject
    lateinit var commentRepository: CommentRepository

    @Inject
    lateinit var dataFileService: DataFileService

    @Inject
    lateinit var dataFileRepository: DataFileRepository

    @Inject
    lateinit var dataFileValidator: Validator<DataFileDTO>

    @Inject
    lateinit var videoFileService: VideoFileService

    @Inject
    lateinit var fileSystemRepository: FileSystemRepository

    @Transactional
    fun setDataFileToQuestionById(questionId: Long, dataFile: DataFileDTO, byteArray: ByteArray) {

        val validated = dataFileValidator.validate(dataFile)

        if (!validated)
            throw BadRequestException("There was an error while adding the file ${dataFile.name}")

        val question = questionRepository.findById(questionId)
            ?: throw NotFoundException("No question with the ID $questionId was found")
        val oldDataFile = question.dataFile
        if (oldDataFile != null) {
            val videoFile = oldDataFile.videoFile
            if (videoFile != null) {
                val fileName = videoFile.fileName
                fileSystemRepository.delete(fileName)
            }
        }


        val fileName = UUID.randomUUID().toString()
        fileSystemRepository.save(byteArray, fileName)
        val videoFile = VideoFile(fileName = fileName,  byteArray)

        videoFileService.save(videoFile)
        dataFile.videoFile = videoFile.id


        val savedDataFile = dataFileService.save(dataFile)
        val persistedDataFile = dataFileRepository.findById(savedDataFile?.id!!)
        persistedDataFile?.videoFile = videoFile
        question.dataFile = persistedDataFile
    }

    @Transactional
    fun deleteById(id: Long) {
        val question =
            questionRepository.findById(id) ?: throw NotFoundException("No question with the ID $id was found.")
        val dataFileId = question.dataFile?.id
        if (dataFileId != null) {
            question.dataFile = null
            dataFileService.deleteById(dataFileId)
        }
        questionRepositoryProxy.deleteById(id)
    }

    @Transactional
    fun update(questionDTO: QuestionDTO): QuestionDTO {
        if (questionDTO.id == null) throw BadRequestException("The id of the question to be updated is missing.")

        try {
            return questionRepositoryProxy.update(questionDTO)
                ?: throw BadRequestException("There was an error while updating")
        } catch (ex: Exception) {
            throw BadRequestException(ex.message)
        }

    }

    @Transactional
    fun addCommentToQuestionById(id: Long, commentDTO: CommentDTO, userId: String): QuestionDTO {
        val question = questionRepository.findById(id)
            ?: throw NotFoundException("There was no question with the ID: $id found")
        if (commentDTO.id != null) throw BadRequestException("The comment has an ID: $id")
        if (commentDTO.question != null) throw BadRequestException("The comment has a question")
        if (commentDTO.content.isEmpty()) throw BadRequestException("The content of the commennt is empty")

        try {
            val user = userRepository.find("userid", userId).firstResult()
            val comment = commentRepositoryProxy.commentMapper.convertDTOToModel(commentDTO)
            comment.keycloakUser = user
            question.comments.add(comment)
            comment.question = question
            commentRepository.save(comment)
        } catch (ex: Exception) {
            throw BadRequestException(ex.message)
        }


        return questionRepositoryProxy.questionMapper.convertModelToDTO(question)
    }

    @Transactional
    fun findById(id: Long): QuestionDTO {
        return questionRepositoryProxy.findById(id) ?: throw NotFoundException("No question with the ID $id was found.")
    }

    @Transactional
    fun deleteAll() {
        questionRepository.purgeAll()
    }

    @Transactional
    fun removeCommentFromQuestionById(questionId: Long, commentId: Long): Boolean {
        val question = questionRepository.findById(questionId)
            ?: throw NotFoundException("No question with the ID $questionId was found.")
        val comment = commentRepository.findById(commentId)
            ?: throw NotFoundException("No comment with the ID $commentId was found.")

        return question.comments.remove(comment)


    }

    @Transactional
    fun findByQuery(page: Int, pageSize: Int, sort: String, dir: String, questionBeanParam: QuestionBeanParam): List<QuestionDTO> {
        return questionRepositoryProxy.findByQuery(page, pageSize, sort, dir, questionBeanParam)
    }


}
