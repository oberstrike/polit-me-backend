package de.maju.subject

import com.maju.openapi.annotations.OASPath
import com.maju.openapi.annotations.OASResource
import com.maju.openapi.codegen.RequestMethod
import de.maju.question.QuestionDTO
import org.eclipse.microprofile.openapi.annotations.media.Content
import org.eclipse.microprofile.openapi.annotations.media.Schema
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody
import javax.enterprise.context.ApplicationScoped
import javax.transaction.Transactional
import javax.validation.Validator
import javax.ws.rs.BadRequestException
import javax.ws.rs.DefaultValue
import javax.ws.rs.PathParam
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType

@ApplicationScoped
@OASResource(path = "/api/subjects", tagName = "Subject")
class SubjectResource(
    private val subjectService: SubjectService,
    private val validator: Validator
) : ISubjectResource {

    @OASPath(path = "/all")
    override fun getSubjects() = subjectService.findAll()


    @OASPath(path = "/id/{id}")
    override fun getSubjectById(@PathParam("id") id: Long): SubjectDTO? {
        return subjectService.findById(id)
    }

    @Transactional
    @OASPath(requestMethod = RequestMethod.POST)
    @RequestBody(
        content = [
            Content(mediaType = MediaType.APPLICATION_JSON)
        ]
    )
    override fun addSubject(@Parameter(schema = Schema(implementation = SubjectDTO::class)) subjectDTO: SubjectDTO): SubjectDTO {
        if (subjectDTO.id != null) throw BadRequestException("The parameter id is accidentally not null.")
        validateSubjectDTO(subjectDTO)
        return subjectService.add(subjectDTO)
    }

    @OASPath(
        requestMethod = RequestMethod.DELETE,
        path = "/purge/id/{id}",
        produces = MediaType.APPLICATION_JSON,
        consumes = MediaType.TEXT_PLAIN
    )
    override fun purgeSubjectById(@PathParam("id") id: Long) {
        subjectService.purgeById(id)
    }


    @OASPath(
        requestMethod = RequestMethod.DELETE,
        path = "/id/{id}",
        produces = MediaType.APPLICATION_JSON,
        consumes = MediaType.TEXT_PLAIN
    )
    override fun deleteById(@PathParam("id") id: Long): SubjectDTO {
        return subjectService.deleteById(id)
            ?: throw BadRequestException("There was an error while deleting the subject with the id: $id")
    }

    @OASPath(requestMethod = RequestMethod.PUT)
    override fun updateSubject(subjectDTO: SubjectDTO): SubjectDTO {
        validateSubjectDTO(subjectDTO)
        val result = subjectService.put(subjectDTO)
        result ?: throw BadRequestException("There was an error while updating the subject: $subjectDTO")
        return result
    }

    @OASPath(path = "/id/{id}/questions", requestMethod = RequestMethod.POST)
    override fun addQuestionBySubjectId(@PathParam("id") id: Long, questionDTO: QuestionDTO): SubjectDTO {
        return subjectService.addQuestionBySubjectId(id, questionDTO)
    }


    @OASPath(path = "/id/{id}/questions")
    override fun getQuestionsBySubjectId(
        @PathParam("id") id: Long,
        @QueryParam("page") @DefaultValue("1") page: Int?,
        @QueryParam("pageSize") @DefaultValue("20") pageSize: Int?
    ): List<QuestionDTO> {
        return subjectService.getQuestionsBySubjectId(id, page ?: 1, pageSize ?: 20)
    }


    private fun validateSubjectDTO(subjectDTO: SubjectDTO) {
        val violations = validator.validate(subjectDTO)
        if (violations.isNotEmpty()) throw BadRequestException(violations.toString())
    }


}
