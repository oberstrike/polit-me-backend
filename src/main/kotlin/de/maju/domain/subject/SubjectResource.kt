package de.maju.domain.subject

import de.maju.domain.question.QuestionDTO
import de.maju.util.PagedRequest
import de.maju.util.SortedRequest
import org.eclipse.microprofile.openapi.annotations.media.Content
import org.eclipse.microprofile.openapi.annotations.media.Schema
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import org.eclipse.microprofile.openapi.annotations.tags.Tags
import javax.ws.rs.*
import javax.ws.rs.core.MediaType

@Path(value = "/api/subjects")
@Tags(Tag(name = "Subject", description = ""))
class SubjectResource(
    private val subjectService: SubjectService
) {

    @GET
    @Produces(value = ["application/json"])
    fun getSubjectsByQuery(
        @BeanParam sortedRequest: SortedRequest,
        @BeanParam pagedRequest: PagedRequest,
        @BeanParam subjectBeanParam: SubjectBeanParam
    ) = subjectService.getSubjectsByQuery(
        sortedRequest = sortedRequest,
        pagedRequest = pagedRequest,
        subjectBeanParam = subjectBeanParam
    )


    @GET
    @Produces(value = ["application/json"])
    @Path(value = "/id/{id}")
    fun getSubjectById(@PathParam("id") id: Long): SubjectDTO? {
        return subjectService.findById(id)
    }

    @POST
    @Produces(value = ["application/json"])
    @Consumes(value = ["application/json"])
    @RequestBody(
        content = [
            Content(mediaType = MediaType.APPLICATION_JSON)
        ]
    )
    fun addSubject(@Parameter(schema = Schema(implementation = SubjectCreateDTO::class)) subjectCreateDTO: SubjectCreateDTO): SubjectDTO
    {
        return subjectService.add(subjectCreateDTO)
    }

    @DELETE
    @Path(value = "/purge/id/{id}")
    fun purgeSubjectById(@PathParam("id") id: Long) {
        subjectService.purgeById(id)
    }


    @DELETE
    @Path(value = "/id/{id}")
    fun deleteSubjectById(@PathParam("id") id: Long): SubjectDTO {
        return subjectService.deleteById(id)
            ?: throw BadRequestException("There was an error while deleting the subject with the id: $id")
    }

    @PUT
    fun updateSubject(subjectDTO: SubjectDTO): SubjectDTO {
        return subjectService.update(subjectDTO)
    }


    @POST
    @Path(value = "/id/{id}/questions")
    fun addQuestionBySubjectId(@PathParam("id") id: Long, questionDTO: QuestionDTO): SubjectDTO {
        return subjectService.addQuestionBySubjectId(id, questionDTO)
    }


    @GET
    @Path(value = "/id/{id}/questions")
    fun getQuestionsBySubjectId(
        @PathParam("id") id: Long,
        @BeanParam pagedRequest: PagedRequest
    ): List<QuestionDTO> {
        return subjectService.getQuestionsBySubjectId(id, pagedRequest.page, pagedRequest.pageSize)
    }


}
