package de.maju.domain.question

import de.maju.domain.subject.SubjectBeanParam
import de.maju.util.ParamCreator
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class QuestionPanacheParamCreator : ParamCreator<QuestionBeanParam> {

    override fun createParams(target: QuestionBeanParam): Map<String, Any> {
        return with(target) {
            mutableMapOf<String, Any>().apply {
                if (created != null) put("created", created)
                if (id != null) put("id", id)
                if (public != null) put("public", public)
                if (owner != null) put("owner", owner)
            }
        }

    }
}
