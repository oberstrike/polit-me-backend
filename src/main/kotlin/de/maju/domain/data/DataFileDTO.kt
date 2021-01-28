package de.maju.domain.data

import com.maju.openapi.annotations.OASProperty
import com.maju.openapi.annotations.OASSchema
import com.maju.openapi.codegen.generators.annotations.schema.OASBaseSchemaEnum
import org.eclipse.microprofile.openapi.annotations.media.Schema

@OASSchema("DataFile")
@Schema(name = "DataFile")
data class DataFileDTO(
    override val id: Long? = null,
    @OASProperty(OASBaseSchemaEnum.BINARY_STRING)
    override var content: String? = null,
    override var name: String,
    override var extension: String
) : IDataFileDTO
