package de.maju.domain.datafile

import com.maju.annotations.RepositoryProxy
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import io.quarkus.panache.common.Page
import io.quarkus.panache.common.Sort
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
@RepositoryProxy(
    converter = DataFileMapper::class
)
class DataFileRepository : PanacheRepository<DataFile> {

    fun save(dataFile: DataFile): DataFile {
        persist(dataFile)
        flush()
        return dataFile
    }

    fun findByQuery(sort: String, direction: String, page: Int, pageSize: Int): List<DataFile> {
        return findAll(Sort.by(sort, Sort.Direction.valueOf(direction))).page(Page.of(page, pageSize)).list()

    }

}
