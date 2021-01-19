package de.maju.util


import de.maju.domain.comments.CommentDTO
import de.maju.domain.comments.CommentRepositoryProxy
import de.maju.domain.comment.CommentController
import de.maju.domain.question.QuestionController
import de.maju.domain.subject.SubjectController
import de.maju.domain.question.QuestionDTO
import de.maju.domain.question.QuestionService
import de.maju.domain.subject.SubjectDTO
import de.maju.domain.subject.SubjectService
import org.eclipse.microprofile.rest.client.inject.RestClient
import org.junit.Assert
import org.junit.jupiter.api.Assertions
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs.core.Form
import kotlin.random.Random

@Transactional
abstract class AbstractRestTest {

    @Inject
    protected lateinit var subjectController: SubjectController

    @Inject
    protected lateinit var questionController: QuestionController

    @Inject
    protected lateinit var commentController: CommentController

    @Inject
    protected lateinit var subjectService: SubjectService

    @Inject
    protected lateinit var questionService: QuestionService

    @Inject
    protected lateinit var keyCloakService: KeyCloakService

    @Inject
    protected lateinit var commentRepositoryProxy: CommentRepositoryProxy

    @RestClient
    @Inject
    protected lateinit var authClient: UserAuthClient


    protected val userDTO = UserDTO(
        username = "test123",
        email = "test123@gmx.de"
    )

    private fun addSubjectDTO(subject: SubjectDTO): SubjectDTO {
        return subjectController.addSubjectDTO(subject)!!
    }

    fun withLoggedIn(
        loginForm: Form,
        block: (jwtToken: JWTToken) -> Unit
    ) {
        val result = authClient.login(
            loginForm
        )

        block.invoke(
            JWTToken(
                result["access_token"] as String,
                result["refresh_token"] as String
            )
        )
    }

    fun withRegistered(block: (user: UserDTO, jwtToken: JWTToken) -> Unit) {
        val password = "test123123!"
        keyCloakService.register(username = userDTO.username, password = password, email = userDTO.email!!)

        withLoggedIn(getLogInForm(username = userDTO.username, password = password)) {
            block.invoke(
                UserDTO(
                    username = userDTO.username,
                    email = userDTO.email
                ), it
            )
        }

        keyCloakService.delete(username = userDTO.username)
    }

    @Transactional
    fun withSubject(subject: SubjectDTO = TestHelper.createSubjectDTO(), block: (subject: SubjectDTO) -> Unit) {
        val saved = addSubjectDTO(subject)
        Assert.assertNotNull(saved)
        block.invoke(saved)
        subjectService.deleteById(saved.id!!)
    }

    @Transactional
    fun withQuestion(
        question: QuestionDTO = TestHelper.createQuestionDTO(),
        subject: SubjectDTO = TestHelper.createSubjectDTO(),
        block: (question: QuestionDTO, subject: SubjectDTO) -> Unit
    ) {
        withSubject(subject) { subjectDTO ->
            val updatedSubject = subjectController.addQuestionToSubject(subjectDTO, question)
            val updatedQuestion = updatedSubject!!.questions.first()
            block.invoke(updatedQuestion, updatedSubject)
            questionService.deleteById(updatedQuestion.id!!)
        }
    }

    fun withComment(commentDTO: CommentDTO = TestHelper.createCommentDTO(), block: (question: QuestionDTO, comment: CommentDTO) -> Unit) {
        withQuestion { question, _ ->
            withRegistered { _, jwtToken ->
                val updatedQuestion =
                    questionController.addCommentToQuestion(question, commentDTO, jwtToken.accessToken)
                val comment = updatedQuestion!!.comments.first()
                block(updatedQuestion, comment)
                val removed =
                    questionService.removeCommentFromQuestionById(updatedQuestion.id!!, comment.id!!)
                Assertions.assertTrue(removed)
                commentRepositoryProxy.deleteById(comment.id!!)
            }
        }
    }

}

object TestHelper {

    fun createSubjectDTO() = SubjectDTO(content = "Content ${Random.nextInt()}")

    fun createQuestionDTO() = QuestionDTO(owner = "Markus", content = ByteArray(Random.nextInt(100)))

    fun createCommentDTO() = CommentDTO(content = "Comment ${Random.nextInt()}")

}
