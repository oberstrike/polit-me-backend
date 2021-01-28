package de.maju.rest.domain.subject

import de.maju.domain.subject.Subject
import de.maju.domain.subject.SubjectMapper
import de.maju.rest.util.TestHelper
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.LocalDateTime
import javax.inject.Inject

@QuarkusTest
class SubjectAfterMapperTest {

    @Inject
    lateinit var subjectMapper: SubjectMapper

    @Test
    fun setDTOAndModelCreatedDateTest() {

        val subjectDTO = TestHelper.createSubjectDTO()
        val subject = Subject()

        val convertedModel = subjectMapper.convertModelToDTO(subject)
        val targetDateTime = LocalDateTime.now()
        Assertions.assertNotEquals(0, convertedModel.created)

        val newSubjectModel = Subject()
        subjectMapper.convertDTOToModel(subjectDTO)

        val duration = Duration.between(targetDateTime, newSubjectModel.created)
        Assertions.assertTrue(duration.seconds < 1)
    }
}
