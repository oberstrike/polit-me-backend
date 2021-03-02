package de.maju.domain.subject

import com.maju.utils.IConverter
import de.maju.domain.question.Question
import de.maju.domain.question.QuestionDTO
import de.maju.domain.question.QuestionMapper
import de.maju.util.LocalDateTimeMapper
import org.mapstruct.*
import javax.enterprise.context.ApplicationScoped

@Mapper(
    uses = [QuestionMapper::class, SubjectAfterMapper::class, LocalDateTimeMapper::class],
    componentModel = "cdi",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
interface SubjectMapper : IConverter<Subject, SubjectDTO>

@Mapper(
    componentModel = "cdi",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
interface SubjectCreateMapper: IConverter<SubjectDTO, SubjectCreateDTO>{
    @Mapping(target = "questions", expression = "java(SubjectCreateMapper.emptyQuestions())")
    override fun convertDTOToModel(dto: SubjectCreateDTO): SubjectDTO

    companion object{
        @JvmStatic
        fun emptyQuestions(): List<QuestionDTO> {
            return emptyList()
        }

    }

}


@ApplicationScoped
class SubjectCreateAfterMapper {

    @AfterMapping
    fun toModel(@MappingTarget model: Subject, dto: SubjectCreateDTO){

    }

}
