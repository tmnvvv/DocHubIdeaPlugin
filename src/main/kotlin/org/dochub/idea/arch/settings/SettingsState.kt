package org.dochub.idea.arch.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@State(name = "org.dochub.idea.settings.DocHubSettings", storages = [Storage("DocHubSettings.xml")])
class SettingsState : PersistentStateComponent<SettingsState> {

    var renderMode = defaultRenderMode
    var renderIsExternal = defaultIsExternalRender
    var serverRendering = defaultRenderServer

    override fun getState(): SettingsState = this

    override fun loadState(state: SettingsState) = XmlSerializerUtil.copyBean(state, this)

    companion object {

        var defaultRenderServer = "http://localhost:8079/svg/"
        var defaultIsExternalRender = false
        var defaultRenderMode = "Smetana"
        var renderModes = arrayOf("Smetana", "ELK", "GraphViz")

        val instance: SettingsState
            get() = ApplicationManager.getApplication().getService(SettingsState::class.java)
    }
}