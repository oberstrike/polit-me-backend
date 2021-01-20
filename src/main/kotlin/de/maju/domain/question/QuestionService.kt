package de.maju.domain.question

import de.maju.admin.UserRepository
import de.maju.domain.comments.CommentDTO
import de.maju.domain.comments.CommentRepository
import de.maju.domain.comments.CommentRepositoryProxy
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs.BadRequestException
import javax.ws.rs.NotFoundException

@ApplicationScoped
class QuestionService {

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var questionRepositoryProxy: QuestionRepositoryProxy

    @Inject
    lateinit var questionRepository: QuestionRepository

    @Inject
    lateinit var commentRepositoryProxy: CommentRepositoryProxy

    @Inject
    lateinit var commentRepository: CommentRepository

    @Transactional
    fun deleteById(id: Long) {
        questionRepositoryProxy.findById(id) ?: throw NotFoundException("No question with the ID $id was found.")
        questionRepositoryProxy.deleteById(id)
    }

    @Transactional
    fun update(questionDTO: QuestionDTO): QuestionDTO {
        if (questionDTO.id == null) throw BadRequestException("The id of the question to be updated is missing.")
        //TODO check whether the content is smaller than 20 MB or greater.

        val maxFileSize = 1024 * 1024 * 20
        if (questionDTO.content.size > maxFileSize) throw BadRequestException("The content of the question ${questionDTO.id} is larger than 20 MB")

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

        //TODO check whether the content is smaller than 20 MB or greater.

        try {
            val user = userRepository.find("userid", userId).firstResult()


            val comment = commentRepositoryProxy.converter.convertDTOToModel(commentDTO)
            comment.user = user
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
