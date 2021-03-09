package de.maju.config

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import de.maju.domain.subject.SubjectDTO
import de.maju.util.PagedResponse
import io.quarkus.jackson.ObjectMapperCustomizer
import javax.inject.Singleton

@Singleton
class CustomModuleCustomizer : ObjectMapperCustomizer {

    override fun customize(objectMapper: ObjectMapper) {
        objectMapper.registerKotlinModule()
        objectMapper.registerModule(SimpleModule().apply { (PagedResponseDeserializier<SubjectDTO>()) })
    }


}

class PagedResponseDeserializier<T> : JsonDeserializer<PagedResponse<T>>() {

    override fun deserialize(p0: JsonParser?, p1: DeserializationContext?): PagedResponse<T> {
        return PagedResponse(emptyList(), 0, 0)
    }

}
