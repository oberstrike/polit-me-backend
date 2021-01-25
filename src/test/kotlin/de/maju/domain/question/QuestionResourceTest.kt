package de.maju.domain.question

import de.maju.domain.comments.CommentDTO
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
class QuestionResourceTest : AbstractRestTest() {

    @AfterEach
    fun clear() {
        questionService.deleteAll()
        subjectService.deleteAll()
    }

    @Test
    fun addAQuestionAndDeleteIt() {
        val question = QuestionDTO(owner = "Markus", dataFile= TestHelper.createDataFileDTO())

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
        withQuestion { question, _ ->
            val owner = "oberstrike"
            val size = 1
            val toUpdate = question.copy(owner = owner, dataFile= TestHelper.createDataFileDTO(size))

            val updated = questionController.updateQuestion(toUpdate)
            Assertions.assertNotNull(updated)
            Assertions.assertNotNull(updated!!.owner)
            Assertions.assertNotNull(updated.dataFile)
            Assertions.assertEquals(size, updated.dataFile!!.content.size)
            Assertions.assertEquals(owner, updated.owner)
        }
    }

    @Test
    fun updateQuestionWithCommentTest() {
        withComment { question, comment ->
            val owner = "New owner"
            val content= TestHelper.createDataFileDTO(2)

            val toUpdate = question.copy(owner = owner, dataFile=content)
            val updated = questionController.updateQuestion(toUpdate)
            Assertions.assertNotNull(updated)
            Assertions.assertNotNull(updated!!.owner)
            Assertions.assertEquals(content.content.size, updated.dataFile!!.content.size)
            Assertions.assertEquals(owner, updated.owner)

            val comments = updated.comments
            Assertions.assertEquals(1, comments.size)

            val updatedComment = comments.first()
            Assertions.assertNotNull(updatedComment)
            Assertions.assertEquals(comment.id!!, updatedComment.id!!)

        }

    }

    @Test
    fun findByIdTest() {
        withQuestion { question, _ ->
            val questionDTO = questionController.getQuestionById(question.id!!)
            Assertions.assertNotNull(questionDTO)
            Assertions.assertNotNull(questionDTO!!.id)
            Assertions.assertEquals(question.id, questionDTO.id)
        }
    }

    @Test
    fun addCommentToQuestionTest_negative() {
        withQuestion { question, _ ->
            val content = "Ein Kommentar"
            val comment = CommentDTO(content = content)

            val updatedQuestion = questionController.addCommentToQuestion(question, comment, "")
            Assertions.assertNull(updatedQuestion)

        }
    }

    @Test
    fun addCommentToQuestionTest() {
        withRegistered { _, jwtToken ->
            withQuestion { question, _ ->
                val content = "Ein Kommentar"
                val comment = CommentDTO(content = content)
                val accessToken = jwtToken.accessToken


                val updatedQuestion = questionController.addCommentToQuestion(question, comment, accessToken)
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
}
