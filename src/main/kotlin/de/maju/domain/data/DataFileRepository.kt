package de.maju.domain.data

import com.maju.annotations.RepositoryProxy
import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
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

}
