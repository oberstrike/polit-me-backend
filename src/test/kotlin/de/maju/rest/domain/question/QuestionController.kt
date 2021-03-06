package de.maju.rest.domain.question

import de.maju.domain.comments.CommentDTO
import de.maju.domain.question.QuestionDTO
import de.maju.domain.question.questionPath
import de.maju.rest.util.Controller
import io.restassured.builder.MultiPartSpecBuilder
import io.restassured.http.ContentType
import javax.activation.MimeType
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.core.MediaType

@ApplicationScoped
class QuestionController {

    @Inject
    lateinit var controller: Controller

    fun getQuestionById(id: Long): QuestionDTO? {
        val response = controller.sendGet("$questionPath/id/$id")

        val statusCode = response.statusCode
        if (statusCode != 200) {
            println(response.body)
            return null
        }
        return controller.fromJson(response.asString())
    }

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
        if (statusCode != 200) {
            println(response.body)
            return null
        }
        return controller.fromJson(response.asString())
    }

    fun addCommentToQuestion(question: QuestionDTO, comment: CommentDTO, accessToken: String): QuestionDTO? {
        val requestJson = controller.toJson(comment)
        val response =
            controller.sendPost(
                "$questionPath/id/${question.id}/comments",
                body = requestJson,
                bearerToken = accessToken,
                file = null
            )
        val statusCode = response.statusCode
        if (statusCode != 200) return null
        return controller.fromJson(response.asString())
    }

    fun addFileToQuestionById(id: Long, name: String): Boolean {
        val multiPartFile = MultiPartSpecBuilder(ByteArray(11))
            .fileName(name)
            .controlName("content")
            .mimeType(MediaType.MULTIPART_FORM_DATA)
            .build()

        val response = controller.sendPost(
            path = "$questionPath/id/$id/file",
            body = "",
            file = multiPartFile
        )
        val statusCode = response.statusCode
        return statusCode == 200

    }
}

inline fun <reified T> Controller.fromJson(json: String): T {
    return objectMapper.readValue(json, T::class.java)
}
