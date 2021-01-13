package de.maju.domain.question

import de.maju.comments.CommentDTO
import de.maju.question.QuestionDTO
import de.maju.util.AbstractRestTest
import de.maju.util.DockerTestResource
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import javax.transaction.Transactional

@QuarkusTestResource(DockerTestResource::class)
@QuarkusTest
@Transactional
class QuestionResourceTest : AbstractRestTest() {

    @AfterEach
    fun clear() {
        questionRepositoryProxy.deleteAll()
        subjectRepositoryProxy.deleteAll()
    }

    @Test
    fun addAQuestionAndDeleteIt() {
        val question = QuestionDTO(owner = "Markus", content = ByteArray(3))

        withSubject {
            val updatedSubject = subjectController.addQuestionToSubject(it, question)
            Assertions.assertNotNull(updatedSubject)
            Assertions.assertEquals(1, updatedSubject!!.questions.size)

            val persistedQuestion = updatedSubject.questions.first()
            val isDeleted = questionController.deleteQuestion(persistedQuestion)
            Assertions.assertTrue(isDeleted)
            val newSubject = subjectController.findById(updatedSubject.id!!)
            Assertions.assertEquals(0, newSubject!!.questions.size)

        }
    }

    @Test
    fun updateQuestionTest() {
        withQuestion {
            val owner = "oberstrike"
            val content = ByteArray(2)
            val toUpdate = it.copy(owner = owner, content = content)

            val updated = questionController.updateQuestion(toUpdate)
            Assertions.assertNotNull(updated)
            Assertions.assertNotNull(updated!!.owner)
            Assertions.assertEquals(content.toList(), updated.content.toList())
            Assertions.assertEquals(owner, updated.owner)
        }
    }

    @Test
    fun findByIdTest() {
        withQuestion {
            val questionDTO = questionController.getQuestionById(it.id!!)
            Assertions.assertNotNull(questionDTO)
            Assertions.assertNotNull(questionDTO!!.id)
            Assertions.assertEquals(it.id, questionDTO.id)
        }
    }

    @Test
    fun addCommentToQuestionTest() {
        withQuestion {
            val content = "Ein Kommentar"
            val comment = CommentDTO(content = content)
            val updatedQuestion = questionController.addCommentToQuestion(it, comment)
            Assertions.assertNotNull(updatedQuestion)

            val comments = updatedQuestion!!.comments
            Assertions.assertEquals(1, comments.size)
            val updatedComment = updatedQuestion.comments.first()
            Assertions.assertEquals(content, updatedComment.content)


            //Check if persisted
            val questionById = questionController.getQuestionById(updatedQuestion.id!!)
            Assertions.assertNotNull(questionById)

            val persistedComments = questionById!!.comments
            Assertions.assertEquals(1, persistedComments.size)
        }
    }
}
