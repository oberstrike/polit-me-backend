package de.maju.domain.file

import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Lob

@Entity
 class VideoFile(
    var fileName: String = "",
    @Lob
    @Column(name = "content", length = 1024 * 1024 * 20)
    var content: ByteArray = ByteArray(1)
) : PanacheEntity()
