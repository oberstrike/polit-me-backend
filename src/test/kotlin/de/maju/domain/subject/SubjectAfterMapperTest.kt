package de.maju.domain.subject

import de.maju.subject.Subject
import de.maju.subject.SubjectAfterMapper
import de.maju.util.TestHelper
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.LocalDateTime

class SubjectAfterMapperTest {

    @Test
    fun setDTOAndModelCreatedDateTest() {

        val afterMapper = SubjectAfterMapper()
        val subjectDTO = TestHelper.createSubjectDTO()

        val targetDateTime = LocalDateTime.now()
        val subject = Subject(targetDateTime)

        afterMapper.setDTOCreated(subjectDTO, subject)
        Assertions.assertNotEquals(0, subjectDTO.created)

        val newSubjectModel = Subject()
        afterMapper.setModelCreated(subjectDTO, newSubjectModel)

        val duration = Duration.between(targetDateTime, newSubjectModel.created)
        Assertions.assertTrue(duration.seconds < 1)
    }
}
