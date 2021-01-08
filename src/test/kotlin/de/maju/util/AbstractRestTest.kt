package de.maju.util


import de.maju.question.QuestionRepositoryProxy
import de.maju.domain.subject.SubjectController
import de.maju.subject.SubjectDTO
import de.maju.subject.SubjectRepositoryProxy
import org.junit.Assert
import javax.inject.Inject
import javax.transaction.Transactional


abstract class AbstractRestTest {

    @Inject
    protected lateinit var subjectController: SubjectController

    @Inject
    protected lateinit var subjectRepositoryProxy: SubjectRepositoryProxy

    @Inject
    protected lateinit var questionRepositoryProxy: QuestionRepositoryProxy

    private val subject = SubjectDTO(content = "Content")

    @Transactional
    fun withSubject(subject: SubjectDTO = TestHelper.createSubject(), block: (subject: SubjectDTO) -> Unit) {
        val saved = subjectController.addSubjectDTO(subject)
        Assert.assertNotNull(saved)
        block.invoke(saved!!)

        subjectRepositoryProxy.delete(subject)

    }

}

object TestHelper {

    fun createSubject() = SubjectDTO(content = "Content")

}
