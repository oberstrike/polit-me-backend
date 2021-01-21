package de.maju.domain.data

import io.quarkus.hibernate.orm.panache.kotlin.PanacheEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Lob

@Entity
class DataFile : PanacheEntity() {

    @Lob
    @Column(name = "content", length = 1000000)
    var content: ByteArray = ByteArray(1)

    var name: String = ""

    var extension: String = ""
}

