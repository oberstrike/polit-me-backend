package de.maju.util

import javax.ws.rs.DefaultValue
import javax.ws.rs.QueryParam

class PagedRequest {
    @DefaultValue("0")
    @QueryParam("page")
    val page: Int = 0

    @DefaultValue("10")
    @QueryParam("pageSize")
    val pageSize: Int = 10
}


class SortedRequest {
    @QueryParam("sort")
    @DefaultValue("id")
    val sort: String = "id"

    @QueryParam("direction")
    @DefaultValue("asc")
    val direction: String = "asc"
}

