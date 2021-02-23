package de.maju.domain.subject

import javax.ws.rs.QueryParam

class SubjectBeanParam {

    @QueryParam("content")
    val content: String? = null

    @QueryParam("created")
    val created: Long? = null

    @QueryParam("deleted")
    val deleted: Boolean? = null

    @QueryParam("headline")
    val headline: String? = null

    @QueryParam("id")
    val id: Long? = null

    @QueryParam("isPublic")
    val isPublic: Boolean? = null

}

