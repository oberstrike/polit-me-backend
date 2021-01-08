package de.maju.domain.question

import de.maju.util.AbstractRestTest
import de.maju.util.DockerTestResource
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.junit.QuarkusTest
import org.junit.jupiter.api.AfterEach
import javax.transaction.Transactional

@QuarkusTestResource(DockerTestResource::class)
@QuarkusTest
@Transactional
class QuestionResourceTest: AbstractRestTest() {

    @AfterEach
    fun clear() {
        questionRepositoryProxy.deleteAll()
        subjectRepositoryProxy.deleteAll()
    }

}
