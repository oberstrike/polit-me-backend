package de.maju.domain.subject

import de.maju.domain.question.fromJson
import de.maju.domain.question.QuestionDTO
import de.maju.util.Controller
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.transaction.Transactional

@ApplicationScoped
class SubjectController {

    @Inject
    lateinit var controller: Controller

    @Inject
    lateinit var subjectRepository: SubjectRepository
    

    fun findById(id: Long): SubjectDTO? {
        val response = controller.sendGet("/api/subjects/id/$id")
        val json = response.body.asString()
        return if(json.isNotEmpty()) controller.fromJson(json) else null
    }

    fun purgeById(id: Long): Int {
        val response = controller.sendDelete("/api/subjects/purge/id/${id}")
        return response.statusCode
    }


    fun getAll(): List<SubjectDTO>? {
        val response = controller.sendGet("/api/subjects/all")
        val statusCode = response.statusCode()
        if(statusCode != 200) return null
        val json = response.body.asString()
        return controller.fromJson<Array<SubjectDTO>>(json).toList()
    }

    fun addSubjectDTO(subjectDTO: SubjectDTO): SubjectDTO? {
        val response = controller.sendPost("/api/subjects", body = controller.toJson(subjectDTO))
        val statusCode = response.statusCode()
        if(statusCode != 200) return null
        val json = response.body.asString()
        return controller.fromJson(json)
    }

    fun deleteById(id: Long): SubjectDTO? {
        val response = controller.sendDelete("/api/subjects/id/$id")
        val statusCode = response.statusCode()
        if(statusCode != 200) return null
        val json = response.body.asString()
        return controller.fromJson(json)
    }

    fun updateSubject(subjectDTO: SubjectDTO): SubjectDTO?{
        val requestJson = controller.toJson(subjectDTO)

        val response = controller.sendPut("/api/subjects", requestJson)
        val statusCode = response.statusCode()
        if(statusCode != 200) {
            println("There was an error $statusCode")
            println("Error: ${response.body.asString()}")
        }

        val responseJson = response.body.asString()
        return controller.fromJson(responseJson)
    }

    fun addQuestionToSubject(subjectDTO: SubjectDTO, questionDTO: QuestionDTO): SubjectDTO?{
        val json = controller.toJson(questionDTO)

        val response = controller.sendPost("/api/subjects/id/${subjectDTO.id}/questions", json)
        val statusCode = response.statusCode()
        if(statusCode != 200) return null

        val responseJson = response.body.asString()
        return controller.fromJson(responseJson)
    }

    fun getQuestionsBySubjectId(subjectDTO: SubjectDTO): List<QuestionDTO>?{
        val response = controller.sendGet("/api/subjects/id/${subjectDTO.id}/questions")
        val statusCode = response.statusCode()
        if(statusCode != 200) return null

        val responseJson = response.body.asString()
        return controller.fromJson(responseJson)
    }

    @Transactional
    fun clear() {
        subjectRepository.deleteAll()
    }

}
