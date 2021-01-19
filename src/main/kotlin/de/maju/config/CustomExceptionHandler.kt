package de.maju.config


import com.main.ResponseError
import io.quarkus.resteasy.runtime.NotFoundExceptionMapper
import io.quarkus.security.UnauthorizedException
import org.jboss.logging.Logger
import javax.servlet.annotation.WebFilter
import javax.ws.rs.BadRequestException
import javax.ws.rs.ForbiddenException
import javax.ws.rs.NotFoundException
import javax.ws.rs.NotSupportedException
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class CustomNotFoundExceptionMapper : NotFoundExceptionMapper() {

    companion object {
        val LOG: Logger = Logger.getLogger(CustomNotFoundExceptionMapper::class.java)
    }

    private val statusCode = 404

    override fun toResponse(exception: NotFoundException): Response {
        val responseError = ResponseError(statusCode, exception.message ?: "")
        LOG.info("There was an error: $statusCode: ${responseError.message}")
        return Response.status(statusCode).entity(responseError).build()
    }
}

@Provider
class CustomExceptionHandler : ExceptionMapper<RuntimeException> {

    companion object {
        val LOG: Logger = Logger.getLogger(CustomExceptionHandler::class.java)
    }


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

        LOG.info("There was an error: $statusCode: ${error.message}")
        return Response.status(statusCode).entity(error).build()
    }
}

class MissingParameterException(msg: String = "") : BadRequestException(msg)



