package de.maju.domain.data

import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Lob

@Entity
class DataFile : PanacheEntity() {

    @Lob
    @Column(name = "content", length = 1024 * 1024 * 20)
    var content: ByteArray = ByteArray(1)

    var name: String = ""

    var extension: String = ""

}

