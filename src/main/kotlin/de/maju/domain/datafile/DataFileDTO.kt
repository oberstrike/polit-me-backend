package de.maju.domain.datafile

import com.maju.openapi.annotations.OASProperty
import com.maju.openapi.annotations.OASSchema
import com.maju.openapi.codegen.generators.annotations.schema.OASBaseSchemaEnum
import org.eclipse.microprofile.openapi.annotations.media.Schema

@OASSchema("DataFile")
@Schema(name = "DataFile")
data class DataFileDTO(
    override val id: Long? = null,
    override var videoFile: Long? = null,
    override var name: String,
    override var extension: String
) : IDataFileDTO
