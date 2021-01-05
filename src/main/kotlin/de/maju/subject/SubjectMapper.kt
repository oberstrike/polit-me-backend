package de.maju.subject

import com.maju.utils.IConverter
import de.maju.question.QuestionMapper
import org.mapstruct.Mapper

@Mapper(uses= [QuestionMapper::class], componentModel = "cdi")
interface SubjectMapper:  IConverter<Subject, SubjectDTO> {

}



