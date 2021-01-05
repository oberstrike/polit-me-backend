package de.maju.subject

import com.maju.openapi.annotations.OASPath
import com.maju.openapi.annotations.OASResource
import com.maju.openapi.codegen.RequestMethod
import org.eclipse.microprofile.openapi.annotations.media.Content
import org.eclipse.microprofile.openapi.annotations.media.Schema
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.transaction.Transactional
import javax.validation.Valid
import javax.validation.Validator
import javax.ws.rs.BadRequestException
import javax.ws.rs.NotFoundException
import javax.ws.rs.PathParam
import javax.ws.rs.core.MediaType

@ApplicationScoped
@OASResource(path = "/api/subject", tagName = "Subject")
class SubjectResource(
    private val subjectService: SubjectService,
    private val validator: Validator
) :ISubjectResource {

    @OASPath(path = "/all")
    override fun findAll() = subjectService.findAll()


    @OASPath(path = "/id/{id}")
    override fun findById(@PathParam("id") id: Long): SubjectDTO? {
        return subjectService.findById(id)
    }

    @Transactional
    @OASPath(requestMethod = RequestMethod.POST)
    @RequestBody(content = [
        Content(mediaType = MediaType.APPLICATION_JSON)
    ])
    override fun add(@Parameter(schema = Schema(implementation = SubjectDTO::class)) subjectDTO: SubjectDTO): SubjectDTO {
        if (subjectDTO.id != null) throw BadRequestException("The parameter id is accidentally not null.")
        validateSubjectDTO(subjectDTO)
        return subjectService.add(subjectDTO)
    }


    @OASPath(requestMethod = RequestMethod.DELETE)
    override fun delete(subjectDTO: SubjectDTO) {
        if (!subjectService.delete(subjectDTO)) {
            throw BadRequestException("There was an error while deleting the subject: $subjectDTO")
        }
    }

    @OASPath(requestMethod = RequestMethod.PUT)
    override fun put(subjectDTO: SubjectDTO): SubjectDTO {
        validateSubjectDTO(subjectDTO)
        val result = subjectService.put(subjectDTO)
        result ?: throw BadRequestException("There was an error while updating the subject: $subjectDTO")
        return result
    }

    private fun validateSubjectDTO(subjectDTO: SubjectDTO) {
        val violations = validator.validate(subjectDTO)
        if (violations.isNotEmpty()) throw BadRequestException(violations.toString())
    }


}
