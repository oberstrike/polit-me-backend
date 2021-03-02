package de.maju.unit.domain

import de.maju.domain.subject.Subject
import de.maju.domain.subject.SubjectMapper
import de.maju.rest.util.TestHelper
import de.maju.unit.Unit
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.TestProfile
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.LocalDateTime
import javax.inject.Inject

@QuarkusTest
@TestProfile(Unit::class)
class SubjectAfterMapperTest {

    @Inject
    lateinit var subjectMapper: SubjectMapper

    @Test
    fun setDTOAndModelCreatedDateTest() {
        val subject = Subject(headline = "Hallo", content = "Freiheit")

        val convertedModel = subjectMapper.convertModelToDTO(subject)
        val targetDateTime = LocalDateTime.now()
        Assertions.assertNotEquals(0, convertedModel.created)

        val newSubjectModel = Subject()
        subjectMapper.convertDTOToModel(convertedModel)

        val duration = Duration.between(targetDateTime, newSubjectModel.created)
        Assertions.assertTrue(duration.seconds < 1)
    }
}
