package org.dochub.idea.arch.tools

import com.fasterxml.jackson.databind.ObjectMapper
import com.intellij.openapi.project.Project
import org.dochub.idea.arch.markline.NavigateMessage
import org.dochub.idea.arch.markline.ON_NAVIGATE_MESSAGE

class JSGateway(project: Project) {

    private val message: MutableMap<String, MutableMap<Any, Any?>> = mutableMapOf()
    var mapper = ObjectMapper()

    init {
        project.messageBus.connect().subscribe(ON_NAVIGATE_MESSAGE,
            object : NavigateMessage {
                override fun go(entity: String?, id: String?) {
                    appendMessage("navigate/$entity", id!!, null)
                }
            })
    }

    fun appendMessage(action: String, id: String, data: Any?) {
        var aData: MutableMap<Any, Any?>? = message[action]
        if (aData == null) {
            aData = mutableMapOf()
            message[action] = aData
        }
        aData[id] = data
    }


    fun pullJSONMessage(): String? {
        val result = if (message.size > 0) mapper.writeValueAsString(message) else null
        message.clear()
        return result
    }
}
