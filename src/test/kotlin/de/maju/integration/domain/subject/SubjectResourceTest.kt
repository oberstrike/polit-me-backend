package de.maju.integration.domain.subject

import de.maju.domain.question.QuestionDTO
import de.maju.domain.subject.SubjectCreateDTO
import de.maju.domain.subject.SubjectDTO
import de.maju.integration.Integration
import de.maju.rest.util.AbstractRestTest
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.TestProfile
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import javax.transaction.Transactional

@QuarkusTest
@Transactional
@TestProfile(Integration::class)
class SubjectResourceTest : AbstractRestTest() {


    @AfterEach
    fun clear() {
        subjectService.deleteAll()
        questionService.deleteAll()
    }

    @Test
    fun addNewSubjectAndListAllTest() {
        val getAll = subjectController.getSubjectsByQuery()
        assert(getAll!!.isEmpty())

        val content = "Content123"
        val added = subjectController.addSubjectDTO(SubjectCreateDTO(content = content, headline = "Frieden"))
        Assertions.assertNotNull(added)
        assert(added!!.content == content)

        val getAllWithAddedSubject = subjectController.getSubjectsByQuery()
        assert(getAllWithAddedSubject != null)

        assert(getAllWithAddedSubject!!.isNotEmpty())
        assert(getAllWithAddedSubject.size == 1)
    }

    @Test
    fun addNewSubjectDTOAndDeleteItAndTryToPurge() {
        val subjectDTO = SubjectCreateDTO(content = "content", headline = "Frieden")
        val oldSubjectDTO = subjectController.addSubjectDTO(subjectDTO)
        Assertions.assertTrue(!oldSubjectDTO!!.deleted)
        val id = oldSubjectDTO.id!!

        val purgeByIdStatusCode = subjectController.purgeById(id)
        Assertions.assertEquals( 400, purgeByIdStatusCode)

        val newSubjectDTOs = subjectController.getSubjectsByQuery(id)
        Assertions.assertNotNull(newSubjectDTOs)
        Assertions.assertEquals(1, newSubjectDTOs!!.size)

        val newSubjectDTO = newSubjectDTOs[0]
        Assertions.assertEquals(false, newSubjectDTO.deleted)
    }


    @Test
    fun addNewSubjectDeleteItAndPurgeItTest() {
        val subjectDTO = SubjectCreateDTO(content = "content", headline = "Frieden")
        val oldSubjectDTO = subjectController.addSubjectDTO(subjectDTO)

        val all = subjectController.getSubjectsByQuery()
        assert(all!!.isNotEmpty())
        assert(all.contains(oldSubjectDTO))

        val id = oldSubjectDTO!!.id!!

        val deleted = id.let {
            subjectController.deleteById(it)
        }

        Assertions.assertNotNull(deleted)
        assert(id == deleted!!.id)
        assert(deleted.deleted)

        val newSubjectDTO = subjectController.findById(id)
        assert(newSubjectDTO != null)
        assert(deleted.deleted)

        subjectController.purgeById(id)

        val test = subjectController.findById(id)
        assert(test == null)

        val findById = subjectService.findById(id)
        assert(findById == null)
    }

    @Test
    fun updateSubject() {
        val newContent = "NewContent123"

        withSubject {
            val copy = it.copy(content = newContent)
            val updatedSubject = subjectController.updateSubject(copy)
            Assertions.assertNotNull(updatedSubject)
            Assertions.assertEquals(newContent, updatedSubject?.content)
        }
    }

    @Test
    fun updateSubjectWithQuestion() {
        val newContent = "NewContent123"

        withQuestion { question, subject ->
            val toUpdate = subject.copy(content = newContent)
            val updatedSubject = subjectController.updateSubject(toUpdate)
            Assertions.assertNotNull(updatedSubject)
            Assertions.assertEquals(newContent, updatedSubject?.content)

            val questions = updatedSubject!!.questions
            Assertions.assertEquals(1, questions.size)

            val newQuestion = questions.first()
            Assertions.assertEquals(question.id, newQuestion.id)

        }
    }

    @Test
    fun addQuestionToSubject() {
        withSubject {
            val question = QuestionDTO(owner = "Markus")

            val resultQuestion = subjectController.addQuestionToSubject(it, question)

            Assertions.assertNotNull(resultQuestion)
            val content = resultQuestion!!.content
            val questions = resultQuestion.questions

            Assertions.assertEquals(it.content, content)
            Assertions.assertEquals(1, questions.size)

            val updatedQuestion = questions.first()
            Assertions.assertNotNull(updatedQuestion)

        }
    }

}
