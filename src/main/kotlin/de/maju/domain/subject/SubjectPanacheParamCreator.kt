package de.maju.domain.subject

import de.maju.util.ParamCreator
import javax.enterprise.context.ApplicationScoped

@ApplicationScoped
class SubjectPanacheParamCreator: ParamCreator<SubjectBeanParam> {

    override fun createParams(target: SubjectBeanParam): Map<String, Any> {
        return with(target){
            mutableMapOf<String, Any>().apply {
                if (content != null) put("content", content)
                if (created != null) put("created", created)
                if (deleted != null) put("isdeleted", deleted)
                if (headline != null) put("headline", headline)
                if (id != null) put("id", id)
                if (isPublic != null) put("ispublic", isPublic)
            }
        }
    }

}
