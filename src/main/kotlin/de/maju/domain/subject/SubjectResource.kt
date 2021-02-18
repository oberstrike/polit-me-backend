package de.maju.domain.subject

import de.maju.domain.question.QuestionDTO
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
        @QueryParam("sort") sort: String?,
        @QueryParam("dir") dir: String?,
        @QueryParam("page") page: Int?,
        @QueryParam("pageSize") pageSize: Int?
    ) = subjectService.getSubjectsByQuery(
        page = page ?: 0,
        pageSize = pageSize ?: 10,
        sort = sort ?: "id",
        direction = dir ?: "asc"
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
    fun addSubject(@Parameter(schema = Schema(implementation = SubjectDTO::class)) subjectDTO: SubjectDTO): SubjectDTO {
        if (subjectDTO.id != null) throw BadRequestException("The parameter id is accidentally not null.")
        return subjectService.add(subjectDTO)
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
        @QueryParam("page") page: Int?,
        @QueryParam("pageSize")  pageSize: Int?
    ): List<QuestionDTO> {
        return subjectService.getQuestionsBySubjectId(id, page ?: 1, pageSize ?: 20)
    }


}
