package de.maju.config


import com.main.ResponseError
import io.quarkus.resteasy.runtime.NotFoundExceptionMapper
import io.quarkus.security.UnauthorizedException
import javax.ws.rs.BadRequestException
import javax.ws.rs.ForbiddenException
import javax.ws.rs.NotFoundException
import javax.ws.rs.NotSupportedException
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class CustomNotFoundExceptionMapper : NotFoundExceptionMapper() {

    private val statusCode = 404

    override fun toResponse(exception: NotFoundException): Response {
        return Response.status(statusCode).entity(ResponseError(statusCode, exception.message ?: "")).build()
    }
}

@Provider
class CustomExceptionHandler : ExceptionMapper<RuntimeException> {
    override fun toResponse(exception: RuntimeException): Response {
        val statusCode = when (exception) {
            is BadRequestException -> 400
            is UnauthorizedException -> 401
            is ForbiddenException -> 403
            is NotSupportedException -> 415
            else -> 400
        }

        val error = ResponseError(
            code = statusCode,
            message = exception.message ?: ""
        )

        return Response.status(statusCode).entity(error).build()
    }
}

class MissingParameterException(msg: String = "") : BadRequestException(msg)



