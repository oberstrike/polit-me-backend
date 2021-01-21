package de.maju.domain.subject

import de.maju.domain.question.QuestionDTO
import de.maju.util.AbstractRestTest
import de.maju.util.DockerTestResource
import de.maju.util.TestHelper
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import javax.transaction.Transactional

@QuarkusTestResource(DockerTestResource::class)
@QuarkusTest
@Transactional
class SubjectResourceTest : AbstractRestTest() {


    @AfterEach
    fun clear() {
        subjectService.deleteAll()
        questionService.deleteAll()
    }


    @Test
    fun addNewSubjectAndListAll() {
        val getAll = subjectController.getAll()
        assert(getAll!!.isEmpty())


        val content = "Content123"
        val added = subjectController.addSubjectDTO(SubjectDTO(content = content))
        Assertions.assertNotNull(added)
        assert(added!!.content == content)

        val getAllWithAddedSubject = subjectController.getAll()
        assert(getAllWithAddedSubject != null)

        assert(getAllWithAddedSubject!!.isNotEmpty())
        assert(getAllWithAddedSubject.size == 1)
    }

    @Test
    fun addNewSubjectDTOAndTryToPurge() {
        val subjectDTO = SubjectDTO(content = "content")
        val oldSubjectDTO = subjectController.addSubjectDTO(subjectDTO)
        val id = oldSubjectDTO!!.id!!

        val purgeById = subjectController.purgeById(id)
        assert(purgeById != 204)
    }


    @Test
    fun addNewSubjectDeleteItAndPurgeItTest() {
        val subjectDTO = SubjectDTO(content = "content")
        val oldSubjectDTO = subjectController.addSubjectDTO(subjectDTO)

        val all = subjectController.getAll()
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

            val result = subjectController.addQuestionToSubject(it, question)
            Assertions.assertNotNull(result)
            Assertions.assertEquals(1, result!!.questions.size)
        }
    }

    @Test
    fun addQuestionToSubjectWithDatafile() {
        withSubject {
            val question = QuestionDTO(owner = "Markus", content = TestHelper.createDataFileDTO(1))

            val resultQuestion = subjectController.addQuestionToSubject(it, question)

            Assertions.assertNotNull(resultQuestion)
            val content = resultQuestion!!.content
            val questions = resultQuestion.questions

            Assertions.assertEquals(it.content, content)
            Assertions.assertEquals(1, questions.size)

            val updatedQuestion = questions.first()
            Assertions.assertNotNull(updatedQuestion)
            Assertions.assertNotNull(updatedQuestion.content)

        }
    }

}
