package de.maju.domain.datafile

import de.maju.util.DataSize
import de.maju.util.Validator
import org.eclipse.microprofile.config.inject.ConfigProperty
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class DataFileValidator(
    @ConfigProperty(name = "datafile.validator.extensions")
    extensionProperty: String
) : Validator<DataFileDTO> {

    val extensions = extensionProperty.split(',').map { it.trim() }

    override fun validate(target: DataFileDTO): Boolean {
        return target.name.length < 0 || extensions.contains(target.extension)
    }

}

