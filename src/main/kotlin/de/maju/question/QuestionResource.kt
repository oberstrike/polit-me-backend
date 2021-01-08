package de.maju.question

import com.maju.openapi.annotations.OASPath
import com.maju.openapi.annotations.OASResource
import com.maju.openapi.codegen.RequestMethod
import de.maju.subject.SubjectDTO
import javax.inject.Inject
import javax.ws.rs.GET

const val questionPath = "/api/questions"


@OASResource(path = questionPath, tagName = "Question")
class QuestionResource(
    private val questionService: QuestionService
) : IQuestionResource {


    @OASPath(requestMethod = RequestMethod.POST)
    override fun deleteQuestion(questionDTO: QuestionDTO): QuestionDTO {
        return questionService.delete(questionDTO)
    }


    @OASPath(requestMethod = RequestMethod.PUT)
    override fun updateQuestion(questionDTO: QuestionDTO): QuestionDTO {
        return questionService.update(questionDTO)
    }


}
