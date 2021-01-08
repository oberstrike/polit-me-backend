package de.maju.domain.subject

import de.maju.question.QuestionDTO
import de.maju.subject.SubjectDTO
import de.maju.util.AbstractRestTest
import de.maju.util.DockerTestResource
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import org.junit.Assert
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
        questionRepositoryProxy.deleteAll()
        subjectRepositoryProxy.deleteAll()
    }


    @Test
    fun addNewSubjectAndListAll() {
        val getAll = subjectController.getAll()
        assert(getAll!!.isEmpty())


        val content = "Content123"
        val added = subjectController.addSubjectDTO(SubjectDTO(content = content))
        Assert.assertNotNull(added)
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

        Assert.assertNotNull(deleted)
        assert(id == deleted!!.id)
        assert(deleted.deleted)

        val newSubjectDTO = subjectController.findById(id)
        assert(newSubjectDTO != null)
        assert(deleted.deleted)

        subjectController.purgeById(id)

        val test = subjectController.findById(id)
        assert(test == null)

        val findById = subjectRepositoryProxy.findById(id)
        assert(findById == null)
    }

    @Test
    fun updatSubject() {
        val newContent = "NewContent123"

        withSubject {
            val copy = it.copy(content = newContent)
            val updatedSubject = subjectController.updateSubject(copy)
            Assert.assertNotNull(updatedSubject)
            Assert.assertEquals(newContent, updatedSubject?.content)
        }
    }

    @Test
    fun addQuestionToSubject() {
        withSubject {
            val question = QuestionDTO(content = ByteArray(1), owner = "Markus")

            val result = subjectController.addQuestionToSubject(it, question)
            Assertions.assertNotNull(result)
            assert(result!!.questions.isNotEmpty())


        }


    }

}
