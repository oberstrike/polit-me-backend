package de.maju.subject

import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.transaction.Transactional

@ApplicationScoped
class SubjectService {

    @Inject
    lateinit var subjectRepository: SubjectRepository

    @Inject
    lateinit var subjectMapper: SubjectMapper

    fun findById(id: Long): SubjectDTO? {
        val subject = subjectRepository.findById(id) ?: return null
        return subjectMapper.convertModelToDTO(subject)
    }

    @Transactional
    fun add(subjectDTO: SubjectDTO): SubjectDTO {
        val subject = subjectMapper.convertDTOToModel(subjectDTO)
        subjectRepository.save(subject)
        return subjectMapper.convertModelToDTO(subject)
    }

    @Transactional
    fun delete(subjectDTO: SubjectDTO): Boolean {
        if (subjectDTO.id == null) return false
        return subjectRepository.deleteById(subjectDTO.id)
    }

    @Transactional
    fun put(subjectDTO: SubjectDTO): SubjectDTO? {
        if (subjectDTO.id == null) return null
        subjectRepository.findById(subjectDTO.id) ?: return null
        val subject = subjectMapper.convertDTOToModel(subjectDTO)
        subjectRepository.persist(subject)
        return subjectDTO
    }
}
