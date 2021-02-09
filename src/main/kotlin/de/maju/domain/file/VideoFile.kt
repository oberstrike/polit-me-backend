package de.maju.domain.file

import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Lob

@Entity
data class VideoFile(
    var fileName: String = "",
    @Lob
    @Column(name = "content", length = 1024 * 1024 * 20)
    private var content: ByteArray = ByteArray(1)
) : PanacheEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is VideoFile) return false

        if (fileName != other.fileName) return false

        return true
    }

    override fun hashCode(): Int {
        return fileName.hashCode()
    }
}
