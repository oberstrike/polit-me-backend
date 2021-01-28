package de.maju.domain.question

import de.maju.domain.admin.KeycloakUserRepository
import de.maju.domain.comments.CommentDTO
import de.maju.domain.comments.CommentRepository
import de.maju.domain.comments.CommentRepositoryProxy
import de.maju.domain.data.DataFileDTO
import de.maju.domain.data.DataFileRepository
import de.maju.domain.data.DataFileRepositoryProxy
import de.maju.util.Validator
import java.io.File
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs.BadRequestException
import javax.ws.rs.NotFoundException
import javax.ws.rs.core.Response

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
    lateinit var dataFileRepositoryProxy: DataFileRepositoryProxy

    @Inject
    lateinit var dataFileRepository: DataFileRepository

    @Inject
    lateinit var dataFileValidator: Validator<DataFileDTO>

    @Transactional
    fun addDataFileToQuestionById(questionId: Long, dataFile: DataFileDTO) {
        if (!dataFileValidator.validate(dataFile))
            throw BadRequestException("There was an error while adding the file ${dataFile.name}")

        val question = questionRepository.findById(questionId)
            ?: throw NotFoundException("No question with the ID $questionId was found")

        val savedDataFile = dataFileRepositoryProxy.save(dataFile)
        val persistedDataFile = dataFileRepository.findById(savedDataFile.id!!)
        question.dataFile = persistedDataFile
    }

    @Transactional
    fun deleteById(id: Long) {
        questionRepositoryProxy.findById(id) ?: throw NotFoundException("No question with the ID $id was found.")
        questionRepositoryProxy.deleteById(id)
    }

    @Transactional
    fun update(questionDTO: QuestionDTO): QuestionDTO {
        if (questionDTO.id == null) throw BadRequestException("The id of the question to be updated is missing.")
        //TODO check whether the content is smaller than 20 MB or greater.

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
            val comment = commentRepositoryProxy.converter.convertDTOToModel(commentDTO)
            comment.keycloakUser = user
            question.comments.add(comment)
            comment.question = question

            commentRepository.save(comment)
        } catch (ex: Exception) {
            throw BadRequestException(ex.message)
        }


        return questionRepositoryProxy.converter.convertModelToDTO(question)
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
    fun findByQuery(page: Int, pageSize: Int): List<QuestionDTO> {
        return questionRepositoryProxy.findByQuery(page, pageSize)
    }


}
