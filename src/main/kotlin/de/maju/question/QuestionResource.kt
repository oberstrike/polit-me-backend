package de.maju.question

import com.maju.openapi.annotations.OASPath
import com.maju.openapi.annotations.OASResource
import de.maju.subject.SubjectDTO
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.PathParam


@Path("/api/v")
class QuestionResource {

    @GET
    fun test(): List<SubjectDTO> {
        return emptyList()
    }


}
