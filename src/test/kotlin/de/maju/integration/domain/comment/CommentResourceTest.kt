package de.maju.integration.domain.comment

import de.maju.integration.Integration
import de.maju.rest.util.AbstractRestTest
import de.maju.rest.util.TestHelper
import io.quarkus.test.junit.QuarkusTest
import io.quarkus.test.junit.TestProfile
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import javax.transaction.Transactional

@QuarkusTest
@Transactional
@TestProfile(Integration::class)
class CommentResourceTest : AbstractRestTest() {

    @Test
    fun commentTest() {
        withComment(TestHelper.createCommentDTO()) { _, comment ->
            Assertions.assertNotNull(comment.id)
        }
    }

    @Test
    fun addCommentToQuestionTest() {
        withQuestion { question, _ ->
            withRegistered { _, jwtToken ->
                val comment = TestHelper.createCommentDTO()
                val updatedQuestion =
                    questionController.addCommentToQuestion(question, comment, jwtToken.accessToken)
                val comments = updatedQuestion!!.comments
                Assertions.assertEquals(1, comments.size)
                val updatedComment = comments.first()
                Assertions.assertEquals(comment.content, updatedComment.content)
            }
        }
    }

    @Test
    fun findCommentTest() {
        withComment { _, comment ->
            val foundComments = commentController.find(0, 1)
            Assertions.assertNotNull(foundComments)
            Assertions.assertEquals(1, foundComments!!.size)
            val foundComment = foundComments.first()
            Assertions.assertEquals(comment.content, foundComment.content)
        }
    }

    @Test
    fun updateCommentTest() {
        withComment { _, comment ->
            val newContent = "new Content"
            val commentToUseToUpdate = comment.copy(content = newContent)
            val updatedComment = commentController.updateComment(commentToUseToUpdate)
            Assertions.assertNotNull(updatedComment)
            Assertions.assertEquals(newContent, updatedComment!!.content)
        }
    }

    @Test
    fun findByIdTest() {
        withComment { _, comment ->
            val findByIdComment = commentController.findById(comment.id!!)
            Assertions.assertNotNull(findByIdComment)
            Assertions.assertEquals(comment.content, findByIdComment!!.content)
        }
    }

    @Test
    fun deleteByIdTest() {
        withQuestion { question, _ ->
            withRegistered { _, jwtToken ->
                val comment = TestHelper.createCommentDTO()
                val updatedQuestion =
                    questionController.addCommentToQuestion(question, comment, jwtToken.accessToken)
                val persistedComment = updatedQuestion!!.comments.first()
                val id = persistedComment.id!!
                val isDeleted = commentController.deleteById(id)
                Assertions.assertTrue(isDeleted)

                //Controll
                val newQuestion = questionController.getQuestionById(updatedQuestion.id!!)
                Assertions.assertEquals(0, newQuestion!!.comments.size)

            }
        }
    }

}
