package de.maju.rest.util

import com.fasterxml.jackson.databind.ObjectMapper
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.When
import io.restassured.response.Response
import io.restassured.specification.MultiPartSpecification
import org.eclipse.microprofile.config.inject.ConfigProperty
import org.keycloak.common.util.MimeTypeUtil
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.core.MediaType


@ApplicationScoped
class Controller {

    @ConfigProperty(name = "quarkus.http.test-port")
    protected var port: Int = 0

    @Inject
    lateinit var objectMapper: ObjectMapper

    fun toJson(obj: Any): String {
        return objectMapper.writeValueAsString(obj)
    }

    fun sendDelete(
        path: String,
        auth: Pair<String, String>? = null,
        bearerToken: String? = null,
        params: Map<String, *>? = null
    ): Response {
        return Given {
            if (auth != null)
                auth().preemptive().basic(auth.first, auth.second)
            if (bearerToken != null)
                auth().preemptive().oauth2(bearerToken)
            if (params != null)
                params(params)
            port(port)
            log().all()
        }.When {
            delete(path)
        }
    }


    fun sendGet(
        path: String,
        auth: Pair<String, String>? = null,
        bearerToken: String? = null,
        params: Map<String, *>? = null
    ): Response {
        return Given {
            if (auth != null)
                auth().preemptive().basic(auth.first, auth.second)
            if (bearerToken != null)
                auth().preemptive().oauth2(bearerToken)
            if (params != null)
                params(params)
            port(port)
            log().all()
        }.When {
            get(path)
        }
    }

    fun sendPut(
        path: String,
        body: String,
        bearerToken: String? = null,
        auth: Pair<String, String>? = null
    ): Response {
        return preparePostBody(auth, bearerToken, body, null).When {
            put(path)
        }
    }

    fun sendPost(
        path: String,
        body: Any,
        bearerToken: String? = null,
        auth: Pair<String, String>? = null
    ): Response {
        return sendPost(
            path = path,
            body = toJson(body),
            bearerToken = bearerToken,
            auth = auth,
            file = null
        )
    }

    fun sendPost(
        path: String,
        body: String? = null,
        bearerToken: String? = null,
        auth: Pair<String, String>? = null,
        file: MultiPartSpecification? = null
    ): Response {
        return preparePostBody(auth, bearerToken, body, file).When {
            post(path)
        }
    }

    private fun preparePostBody(
        auth: Pair<String, String>?,
        bearerToken: String?,
        body: String?,
        file: MultiPartSpecification?
    ) = Given {
        if (auth != null)
            auth().preemptive().basic(auth.first, auth.second)
        if (bearerToken != null)
            auth().preemptive().oauth2(bearerToken)
        port(port)
        log().all()
        if (body != null) {
            body(body)
        }
        if (file != null) {
            multiPart(file)
        }

        if(file != null){
            contentType(MediaType.MULTIPART_FORM_DATA)
        }else{
            contentType(ContentType.JSON)
        }
    }

    fun sendPatch(
        path: String,
        body: String,
        auth: Pair<String, String>? = null,
        params: Map<String, *>? = null
    ): Response {
        return Given {
            if (auth != null)
                auth().preemptive().basic(auth.first, auth.second)
            port(port)
            if (params != null)
                params(params)
            log().all()
            body(body)
            contentType(ContentType.JSON)
        }.When {
            patch(path)
        }

    }
}
