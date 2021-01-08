package de.maju.question

import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs.BadRequestException

@ApplicationScoped
class QuestionService {


    @Inject
    lateinit var questionRepositoryProxy: QuestionRepositoryProxy

    @Inject
    lateinit var questionRepository: QuestionRepository

    @Transactional
    fun delete(questionDTO: QuestionDTO): QuestionDTO {
        if (questionDTO.id != null) throw BadRequestException("An Id was added by mistake.")
        return questionRepositoryProxy.save(questionDTO)
    }

    @Transactional
    fun update(questionDTO: QuestionDTO): QuestionDTO {
        if (questionDTO.id == null) throw BadRequestException("The id of the question to be updated is missing.")

        return questionRepositoryProxy.update(questionDTO)
            ?: throw BadRequestException("There was an error while updating")
    }


}
