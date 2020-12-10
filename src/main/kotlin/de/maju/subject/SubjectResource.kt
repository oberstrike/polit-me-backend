package de.maju.subject

import com.maju.openapi.annotations.OASPath
import com.maju.openapi.annotations.OASResource
import com.maju.openapi.codegen.RequestMethod
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.transaction.Transactional
import javax.validation.Valid
import javax.validation.Validator
import javax.ws.rs.BadRequestException
import javax.ws.rs.NotFoundException
import javax.ws.rs.PathParam

@ApplicationScoped
@OASResource(path = "/api/subject", tagName = "Subject")
class SubjectResource : ISubjectResource {

    @Inject
    lateinit var subjectService: SubjectService

    @Inject
    lateinit var validator: Validator

    @OASPath(path = "/id/{id}")
    override fun findById(@PathParam("id") id: Long): ISubjectDTO? {
        return subjectService.findById(id)
    }

    @Transactional
    @OASPath(requestMethod = RequestMethod.POST)
    override fun add(subjectDTO: SubjectDTO): ISubjectDTO {
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
    override fun put(subjectDTO: SubjectDTO): ISubjectDTO {
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
