package de.maju.domain.data

import com.maju.openapi.annotations.OASProperty
import com.maju.openapi.annotations.OASSchema
import com.maju.openapi.codegen.generators.annotations.schema.OASBaseSchemaEnum
import org.eclipse.microprofile.openapi.annotations.media.Schema

@OASSchema("DataFile")
@Schema(name = "DataFile")
data class DataFileDTO(
    override val id: Long? = null,
    override val content: ByteArray = ByteArray(1),
    override var name: String,
    override var extension: String
) : IDataFileDTO {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is DataFileDTO) return false

        if (id != other.id) return false
        if (!content.contentEquals(other.content)) return false
        if (name != other.name) return false
        if (extension != other.extension) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + content.contentHashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + extension.hashCode()
        return result
    }
}
