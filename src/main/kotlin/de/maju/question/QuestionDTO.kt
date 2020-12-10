package de.maju.question

import de.maju.subject.SubjectDTO

data class QuestionDTO(
    var id: Long,
    val content: ByteArray,
    val subject: Long
)
