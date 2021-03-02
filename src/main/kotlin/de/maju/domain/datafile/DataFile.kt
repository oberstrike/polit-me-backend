package de.maju.domain.datafile

import de.maju.domain.file.VideoFile
import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import javax.persistence.*

@Entity
class DataFile : PanacheEntity() {

    @OneToOne(fetch = FetchType.LAZY,
        cascade = [CascadeType.REMOVE, CascadeType.REFRESH],
        orphanRemoval = true)
    var videoFile: VideoFile? = null

    var name: String = ""

    var extension: String = ""

}

