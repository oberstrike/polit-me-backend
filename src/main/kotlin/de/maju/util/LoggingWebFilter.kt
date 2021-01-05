package de.maju.util

import org.jboss.logging.Logger
import javax.enterprise.context.ApplicationScoped
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest

@ApplicationScoped
class LoggingWebFilter : Filter {


    companion object {
        val LOG: Logger = Logger.getLogger(WebFilter::class.java)
    }

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpServletRequest = request as HttpServletRequest
        val uri = httpServletRequest.requestURI
        val method = httpServletRequest.method
        val contentLength = httpServletRequest.contentLength

        LOG.info("[URI: $uri, method: $method, query: ${request.queryString}, content-length: $contentLength ]")
        chain.doFilter(request, response)
    }

}

