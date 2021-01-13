package de.maju.question

import com.maju.openapi.annotations.OASPath
import com.maju.openapi.annotations.OASResource
import com.maju.openapi.codegen.RequestMethod
import de.maju.comments.CommentDTO
import javax.ws.rs.PathParam

const val questionPath = "/api/questions"


@OASResource(path = questionPath, tagName = "Question")
class QuestionResource(
    private val questionService: QuestionService
) : IQuestionResource {


    @OASPath(requestMethod = RequestMethod.DELETE, path = "/id/{id}")
    override fun deleteQuestion(@PathParam("id") id: Long) {
        questionService.deleteById(id)
    }


    @OASPath(requestMethod = RequestMethod.PUT)
    override fun updateQuestion(questionDTO: QuestionDTO): QuestionDTO {
        return questionService.update(questionDTO)
    }

    @OASPath(path = "/id/{id}")
    override fun getQuestionById(@PathParam("id") id: Long): QuestionDTO {
        return questionService.findById(id)
    }

    @OASPath(path = "/id/{id}", requestMethod = RequestMethod.POST)
    override fun addCommentToQuestionById(@PathParam("id") id: Long, commentDTO: CommentDTO): QuestionDTO {
        return questionService.addCommentToQuestionById(id, commentDTO)
    }

}
