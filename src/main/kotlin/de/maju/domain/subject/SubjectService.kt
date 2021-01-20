package de.maju.domain.subject

import de.maju.domain.question.QuestionDTO
import de.maju.domain.question.QuestionRepository
import de.maju.domain.question.QuestionRepositoryProxy
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.transaction.Transactional
import javax.ws.rs.BadRequestException
import javax.ws.rs.NotFoundException

@ApplicationScoped
class SubjectService {

    @Inject
    lateinit var subjectRepositoryProxy: SubjectRepositoryProxy

    @Inject
    lateinit var subjectRepository: SubjectRepository

    @Inject
    lateinit var questionRepositoryProxy: QuestionRepositoryProxy

    @Inject
    lateinit var questionRepository: QuestionRepository

    fun findById(id: Long): SubjectDTO? {
        return subjectRepositoryProxy.findById(id)
    }

    @Transactional
    fun add(subjectDTO: SubjectDTO): SubjectDTO {
        if (subjectDTO.id != null) throw BadRequestException("An ID was entered by mistake.")
        if (subjectDTO.created != 0L) throw BadRequestException("A date was entered accidentally.")
        return subjectRepositoryProxy.save(subjectDTO)
    }

    @Transactional
    fun deleteById(id: Long): SubjectDTO? {
        val subject = subjectRepository.findById(id) ?: throw NotFoundException("The subject: $id was not found.")

        subject.isDeleted = true
        subjectRepository.persist(subject)
        return subjectRepositoryProxy.converter.convertModelToDTO(subject)
    }

    @Transactional
    fun update(subjectDTO: SubjectDTO): SubjectDTO {
        if (subjectDTO.id == null) throw BadRequestException("There is no ID given")
        return subjectRepositoryProxy.update(subjectDTO)
            ?: throw BadRequestException("There was an error while updating $subjectDTO")
    }

    @Transactional
    fun findAll(): List<SubjectDTO> {
        return subjectRepositoryProxy.getAll()
    }

    @Transactional
    fun purgeById(id: Long) {
        val subject = subjectRepository.findById(id) ?: throw NotFoundException("The subject: $id was not found.")
        if (!subject.isDeleted) {
            throw BadRequestException("The subject: $id is not marked as deleted.")
        }
        val deleted = subjectRepository.deleteById(id)
        if (!deleted) {
            throw BadRequestException("There was an error while purging subject: $id")
        }
    }

    @Transactional
    fun addQuestionBySubjectId(id: Long, questionDTO: QuestionDTO): SubjectDTO {
        //TODO check whether the content is smaller than 20 MB or greater.
        if (questionDTO.content.size > 1024 * 1024 * 20) throw BadRequestException("The filesize is larger than 20 MB")

        val subject =
            subjectRepository.findById(id) ?: throw NotFoundException("No subject with the id $id was found.")

        if (questionDTO.id != null) throw BadRequestException("The requested question has already an ID.")
        val question = questionRepositoryProxy.converter.convertDTOToModel(questionDTO)

        subject.addQuestion(question)
        questionRepository.save(question)
        return subjectRepositoryProxy.converter.convertModelToDTO(subject)
    }

    fun getQuestionsBySubjectId(id: Long, page: Int, pageSize: Int): List<QuestionDTO> {
        return questionRepositoryProxy.findBySubjectId(id, page, pageSize)

    }

    @Transactional
    fun deleteAll() {
        subjectRepository.purgeAll()
    }
}
