package de.maju.domain.comments

import de.maju.domain.question.QuestionService
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs.BadRequestException
import javax.ws.rs.NotFoundException

@ApplicationScoped
class CommentService {

    @Inject
    lateinit var commentRepositoryProxy: CommentRepositoryProxy

    @Inject
    lateinit var questionService: QuestionService

    @Transactional
    fun deleteById(commentId: Long): Boolean {
        val comment = commentRepositoryProxy.findById(commentId)
            ?: throw NotFoundException("There was no comment with the id $commentId found")
        val questionId = comment.question ?: throw BadRequestException("There was no question connected to the comment")
        return questionService.removeCommentFromQuestionById(questionId, commentId)
    }

    fun findById(commentId: Long): CommentDTO {
        return commentRepositoryProxy.findById(commentId)
            ?: throw NotFoundException("There was no comment with the id $commentId found")
    }


    @Transactional
    fun update(commentDTO: CommentDTO): CommentDTO {
        if (commentDTO.id == null) throw BadRequestException("There is no id given")
        return commentRepositoryProxy.update(commentDTO)?: throw BadRequestException("There was an error while updating")
    }

    fun findAll(page: Int, pageSize: Int): List<CommentDTO> {
        return commentRepositoryProxy.findByQuery(page, pageSize)
    }

}
