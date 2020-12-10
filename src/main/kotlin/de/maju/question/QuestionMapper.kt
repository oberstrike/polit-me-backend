package de.maju.question

import de.maju.subject.SubjectMapper
import de.maju.subject.SubjectRepository
import de.maju.util.IMapper
import org.mapstruct.AfterMapping
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@Mapper(uses=[QuestionAfterMapper::class],  componentModel = "cdi")
interface QuestionMapper : IMapper<Question, QuestionDTO> {

    @Mapping(target = "subject", ignore = true)
    override fun convertDTOToModel(dto: QuestionDTO): Question

    @Mapping(target = "subject", ignore = true)
    override fun convertModelToDTO(model: Question): QuestionDTO

}

@ApplicationScoped
class QuestionAfterMapper{

    @Inject
    lateinit var subjectRepository: SubjectRepository

    @AfterMapping
    fun setSubject(dto: QuestionDTO, @MappingTarget question: Question){
        val subjectId = dto.subject
        val subject = subjectRepository.findById(subjectId)
        if(subject != null){
            question.subject = subject
        }
    }

    @AfterMapping
    fun setSubjectId(@MappingTarget dto: QuestionDTO,  question: Question){
        val subjectId = question.subject?.id
        if(subjectId != null){
            dto.id = subjectId
        }
    }

}
