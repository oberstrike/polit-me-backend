package de.maju.subject

import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.transaction.Transactional

@ApplicationScoped
class SubjectService {

    @Inject
    lateinit var subjectRepositoryProxy: SubjectRepositoryProxy


    fun findById(id: Long): SubjectDTO? {
        return subjectRepositoryProxy.findById(id)
    }

    @Transactional
    fun add(subjectDTO: SubjectDTO): SubjectDTO {
        return subjectRepositoryProxy.save(subjectDTO)
    }

    @Transactional
    fun delete(subjectDTO: SubjectDTO): Boolean {
        if (subjectDTO.id == null) return false
        return subjectRepositoryProxy.deleteById(subjectDTO.id)
    }

    @Transactional
    fun put(subjectDTO: SubjectDTO): SubjectDTO? {
        if (subjectDTO.id == null) return null
        return subjectDTO
    }

    fun findAll(): List<SubjectDTO> {
        return subjectRepositoryProxy.getAll()
    }
}
