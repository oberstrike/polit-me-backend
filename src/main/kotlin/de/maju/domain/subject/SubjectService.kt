package de.maju.domain.subject

import de.maju.domain.question.QuestionDTO
import de.maju.domain.question.QuestionRepository
import de.maju.domain.question.QuestionRepositoryProxy
import de.maju.util.PagedRequest
import de.maju.util.SortedRequest
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

    @Inject
    lateinit var subjectCreateMapper: SubjectCreateMapper

    fun findById(id: Long): SubjectDTO? {
        return subjectRepositoryProxy.findById(id)
    }

    @Transactional
    fun add(subjectCreateDTO: SubjectCreateDTO): SubjectDTO {
        val subjectDTO = subjectCreateMapper.convertDTOToModel(subjectCreateDTO)
        return subjectRepositoryProxy.save(subjectDTO)
    }

    @Transactional
    fun deleteById(id: Long): SubjectDTO? {
        val subject =
            subjectRepository.findById(id) ?: throw NotFoundException("The subject: $id was not found.")

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
    fun purgeById(id: Long) {
        val subject =
            subjectRepository.findById(id) ?: throw NotFoundException("The subject: $id was not found.")
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

        val subject =
            subjectRepository.findById(id) ?: throw NotFoundException("No subject with the id $id was found.")

        if (questionDTO.id != null) throw BadRequestException("The requested question has already an ID.")

        val oldQuestion = questionRepositoryProxy.converter.convertDTOToModel(questionDTO)
        oldQuestion.subject = subject
        questionRepository.save(oldQuestion)

        return subjectRepositoryProxy.findById(id) ?: throw BadRequestException("")
    }

    fun getQuestionsBySubjectId(id: Long, page: Int, pageSize: Int): List<QuestionDTO> {
        return questionRepositoryProxy.findBySubjectId(id, page, pageSize)

    }

    @Transactional
    fun deleteAll() {
        subjectRepository.purgeAll()
    }

    @Transactional
    fun getSubjectsByQuery(
        sortedRequest: SortedRequest,
        pagedRequest: PagedRequest,
        query: SubjectBeanParam
    ): List<SubjectDTO> {
        return subjectRepositoryProxy.findByQuery(sortedRequest, pagedRequest, query)

    }
}
