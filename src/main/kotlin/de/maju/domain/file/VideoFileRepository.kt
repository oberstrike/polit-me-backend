package de.maju.domain.file

import io.quarkus.hibernate.orm.panache.kotlin.PanacheRepository
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class VideoFileRepository: PanacheRepository<VideoFile> {

}
