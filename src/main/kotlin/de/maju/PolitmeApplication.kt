package de.maju

import io.quarkus.runtime.Quarkus
import io.quarkus.runtime.QuarkusApplication
import io.quarkus.runtime.annotations.QuarkusMain
import org.eclipse.microprofile.openapi.annotations.Components
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType
import org.eclipse.microprofile.openapi.annotations.info.Contact
import org.eclipse.microprofile.openapi.annotations.info.Info
import org.eclipse.microprofile.openapi.annotations.info.License
import org.eclipse.microprofile.openapi.annotations.media.Schema
import org.eclipse.microprofile.openapi.annotations.security.SecurityRequirement
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme
import org.eclipse.microprofile.openapi.annotations.servers.Server
import javax.ws.rs.core.Application

const val securitySchemeName =  "openIdConnect"

@QuarkusMain
class Main {

    companion object {
        @JvmStatic
        fun main(vararg args: String) {
            Quarkus.run(PolitmeApplication::class.java, *args)
        }
    }
}

@OpenAPIDefinition(
    info = Info(
        title = "Learing-By-Doing-API",
        version = "0.0.1preAlpha",
        contact = Contact(
            name = "oberstrike",
            email = "oberstrike@gmail.com"
        ),
        license = License(
            name = "Apache 2.0",
            url = "http://www.apache.org/licenses/LICENSE-2.0.html"
        )
    ),
    components = Components(
        securitySchemes = [
            SecurityScheme(
                securitySchemeName = securitySchemeName,
                type = SecuritySchemeType.OPENIDCONNECT,
                openIdConnectUrl = "http://localhost:8280/auth/realms/quarkus/.well-known/openid-configuration"
            )
        ]
    ),
    security = [
        SecurityRequirement(name = "openIdConnect", scopes = [])
    ]
)
open class PolitmeApplication : QuarkusApplication, Application() {

    override fun run(vararg args: String?): Int {
        Quarkus.waitForExit()
        return 0
    }
}

