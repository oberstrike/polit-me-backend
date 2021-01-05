package de.maju.util

import de.maju.subject.SubjectMapper
import de.maju.subject.SubjectRepository
import de.maju.subject.SubjectRepositoryProxy
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.inject.Produces

@ApplicationScoped
class Config {

    @Produces
    fun subjectRepositoryProxy(converter: SubjectMapper, subjectRepository: SubjectRepository) =
        SubjectRepositoryProxy(converter, subjectRepository)

}
