package de.maju.rest.domain.datafile

import de.maju.domain.comments.CommentDTO
import de.maju.domain.comments.commentPath
import de.maju.domain.datafile.DataFileDTO
import de.maju.domain.datafile.dataFilePath
import de.maju.rest.domain.question.fromJson
import de.maju.rest.util.Controller
import java.io.File
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.QueryParam

@ApplicationScoped
class DataFileController {

    @Inject
    lateinit var controller: Controller


    fun getDataFileById(id: Long): DataFileDTO? {
        val response = controller.sendGet("$dataFilePath/id/$id")
        val statusCode = response.statusCode
        if (statusCode != 200) return null
        return controller.fromJson(response.body.asString())
    }

    fun getVideoFileByDataFileId(id: Long): File? {
        val response = controller.sendGet("$dataFilePath/id/$id/file")
        val statusCode = response.statusCode
        if (statusCode != 200) return null
        return controller.fromJson(response.body.asString())
    }

    fun getDataFilesByQuery(
        sort: String?,
        dir: String?,
        page: Int?,
        pageSize: Int?
    ): List<DataFileDTO>? {
        val response = controller.sendGet(
            dataFilePath, params = mapOf(
                "page" to page,
                "pageSize" to pageSize,
                "sort" to sort,
                "dir" to dir
            )
        )
        val statusCode = response.statusCode
        if (statusCode != 200) return null
        return controller.fromJson<Array<DataFileDTO>>(response.body.asString()).toList()
    }

}
