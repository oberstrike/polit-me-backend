package de.maju.domain.question

import de.maju.domain.comments.CommentRepository
import de.maju.domain.datafile.DataFileDTO
import de.maju.domain.datafile.DataFileMapper
import de.maju.domain.datafile.DataFileRepository
import de.maju.domain.subject.SubjectRepository
import de.maju.util.Validator
import org.mapstruct.AfterMapping
import org.mapstruct.MappingTarget
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class QuestionAfterMapper {

    @Inject
    lateinit var subjectRepository: SubjectRepository

    @Inject
    lateinit var commentRepository: CommentRepository

    @Inject
    lateinit var dataFileRepository: DataFileRepository

    @Inject
    lateinit var dataFileMapper: DataFileMapper

    @Inject
    lateinit var dataFileValidator: Validator<DataFileDTO>

    @AfterMapping
    fun mapDTO(dto: QuestionDTO, @MappingTarget question: Question) {
        // question.created = LocalDateMapper.asLocalDateTime(dto.created) ?: LocalDateTime.now()

        val subjectId = dto.subject ?: return
        val subject = subjectRepository.findById(subjectId)
        if (subject != null) {
            question.subject = subject
        }

        question.comments.clear()
        val commentIds = dto.comments.map { it.id }
        for (commentId in commentIds) {
            if (commentId != null) {
                val comment = commentRepository.findById(commentId)
                if (comment != null) {
                    question.comments.add(comment)
                }
            }
        }

        val dataFileDTOId = dto.dataFile
        if (dataFileDTOId != null) {
            val dataFile = dataFileRepository.findById(dataFileDTOId)
            if (dataFile != null) {
                question.dataFile = dataFile
            }
        }

    }

    @AfterMapping
    fun mapModel(@MappingTarget dto: QuestionDTO, question: Question) {
        //Subject
        val subjectId = question.subject?.id
        if (subjectId != null) {
            dto.subject = subjectId
        }
        dto.dataFile = question.dataFile?.id

    }

}
