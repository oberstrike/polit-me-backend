package de.maju.util


import de.maju.domain.question.QuestionController
import de.maju.question.QuestionRepositoryProxy
import de.maju.domain.subject.SubjectController
import de.maju.question.QuestionDTO
import de.maju.subject.SubjectDTO
import de.maju.subject.SubjectRepositoryProxy
import org.junit.Assert
import javax.inject.Inject
import javax.transaction.Transactional


abstract class AbstractRestTest {

    @Inject
    protected lateinit var subjectController: SubjectController

    @Inject
    protected lateinit var questionController: QuestionController

    @Inject
    protected lateinit var subjectRepositoryProxy: SubjectRepositoryProxy

    @Inject
    protected lateinit var questionRepositoryProxy: QuestionRepositoryProxy



    @Transactional
    fun withSubject(subject: SubjectDTO = TestHelper.createSubjectDTO(), block: (subject: SubjectDTO) -> Unit) {
        val saved = subjectController.addSubjectDTO(subject)
        Assert.assertNotNull(saved)
        block.invoke(saved!!)
        subjectRepositoryProxy.deleteById(saved.id!!)
    }

    @Transactional
    fun withQuestion(question: QuestionDTO = TestHelper.createQuestionDTO(), block: (question: QuestionDTO) -> Unit) {
        withSubject { subject ->
            val updatedSubject = subjectController.addQuestionToSubject(subject, question)
            val updatedQuestion = updatedSubject!!.questions.first()
            block.invoke(updatedQuestion)
            questionRepositoryProxy.deleteById(updatedQuestion.id!!)
        }
    }

}

object TestHelper {

    fun createSubjectDTO() = SubjectDTO(content = "Content")

    fun createQuestionDTO() = QuestionDTO(owner = "Markus", content = ByteArray(1))


}
