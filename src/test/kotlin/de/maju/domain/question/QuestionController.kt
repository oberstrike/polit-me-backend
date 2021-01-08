package de.maju.domain.question

import de.maju.question.QuestionDTO
import de.maju.question.questionPath
import de.maju.util.Controller
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class QuestionController {

    @Inject
    lateinit var controller: Controller


    fun deleteQuestion(question: QuestionDTO): Boolean {
        val response = controller.sendDelete("$questionPath/id/${question.id}")
        val statusCode = response.statusCode
        if (statusCode != 204) return false
        return true
    }

    fun updateQuestion(question: QuestionDTO): QuestionDTO? {
        val requestJson = controller.toJson(question)
        val response = controller.sendPut(questionPath, requestJson)
        val statusCode = response.statusCode
        if(statusCode != 200) return null
        return controller.fromJson(response.asString())
    }
}

inline fun <reified T> Controller.fromJson(json: String): T {
    return objectMapper.readValue(json, T::class.java)
}
