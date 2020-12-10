package de.maju.subject

import de.maju.question.Question
import de.maju.question.QuestionMapper
import de.maju.util.IMapper
import org.mapstruct.Mapper
import javax.enterprise.context.ApplicationScoped

@Mapper(uses= [QuestionMapper::class], componentModel = "cdi")
interface SubjectMapper : IMapper<Subject, SubjectDTO> {

}



