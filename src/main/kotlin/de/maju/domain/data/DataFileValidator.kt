package de.maju.domain.data

import de.maju.util.DataSize
import de.maju.util.Validator
import org.eclipse.microprofile.config.inject.ConfigProperty
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class DataFileValidator(
    @ConfigProperty(name = "validator.datafile.maxFileSize") private val maxFileSizeString: String,
) : Validator<DataFileDTO> {

    private val maxFileSize = DataSize.parse(maxFileSizeString)

    override fun validate(target: DataFileDTO): Boolean {
        if (target.content == null) {
            return false
        }

        if (target.content!!.toByteArray().size > maxFileSize.bytes) {
            return false
        }

        if (target.name.length < 0) {
            return false
        }


        return true
    }

}

