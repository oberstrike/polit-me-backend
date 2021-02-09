package de.maju.domain.datafile

import org.eclipse.microprofile.config.inject.ConfigProperty
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import javax.enterprise.context.ApplicationScoped
import org.flywaydb.core.internal.resource.filesystem.FileSystemResource
import java.io.File
import java.lang.Exception
import javax.inject.Inject
import javax.ws.rs.BadRequestException
import javax.ws.rs.ServerErrorException


@ApplicationScoped
class FileSystemRepository(
    @ConfigProperty(name = "datafile.folder")
    private val path: String
) {


    fun save(content: ByteArray, name: String): String {
        val newFile = Paths.get("$path\\$name")
        Files.createDirectories(newFile.parent)

        Files.write(newFile, content)
        return newFile.toAbsolutePath().toString()

    }

    fun findByName(name: String): File? {
        return try {
            val file = File("$path$name")
            file
        } catch (e: Exception) {
            // Handle access or file not found problems.
            throw BadRequestException("There was an error will finding the file $name")
        }
    }


    fun delete(name: String) {
        val newFile = Paths.get("$path\\$name")
        Files.delete(newFile)
    }

}
