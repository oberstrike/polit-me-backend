package de.maju.question

import de.maju.comments.CommentDTO
import de.maju.comments.CommentRepository
import de.maju.comments.CommentRepositoryProxy
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs.BadRequestException
import javax.ws.rs.NotFoundException

@ApplicationScoped
class QuestionService {


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

        try {
            return questionRepositoryProxy.update(questionDTO)
                ?: throw BadRequestException("There was an error while updating")
        } catch (ex: Exception) {
            throw BadRequestException(ex.message)
        }

    }

    @Transactional
    fun addCommentToQuestionById(id: Long, commentDTO: CommentDTO): QuestionDTO {
        val question = questionRepository.findById(id)
            ?: throw NotFoundException("There was no question with the ID: $id found")
        if (commentDTO.id != null) throw BadRequestException("The comment has an ID: $id")
        if (commentDTO.question != null) throw BadRequestException("The comment has a question")
        if (commentDTO.content.isEmpty()) throw BadRequestException("The content of the commennt is empty")

        try {
            val comment = commentRepositoryProxy.converter.convertDTOToModel(commentDTO)
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
        return questionRepositoryProxy.findById(id)?: throw NotFoundException("No question with the ID $id was found.")
    }


}
