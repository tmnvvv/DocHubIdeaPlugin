package org.dochub.idea.arch.settings

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.ConfigurationException
import com.intellij.openapi.util.NlsContexts.ConfigurableName
import org.jetbrains.annotations.Nls
import javax.swing.JComponent

class SettingConfigurable : Configurable {

    private var settingComponent: SettingComponent? = null

    @Nls(capitalization = Nls.Capitalization.Title)
    override fun getDisplayName(): @ConfigurableName String {
        return "DocHub: External server rendering"
    }

    override fun getPreferredFocusedComponent(): JComponent? = settingComponent?.let{ getPreferredFocusedComponent() }


    override fun createComponent(): JComponent? {
        settingComponent = SettingComponent()
        return settingComponent?.myMainPanel
    }

    override fun isModified(): Boolean {
        val settingsState: SettingsState = SettingsState.instance
        var modified: Boolean = !(settingComponent?.renderServerText == settingsState.serverRendering)
        modified = modified or !(settingComponent?.renderModeText == settingsState.renderMode)
        modified = modified or (settingComponent?.isExternalRenderBool != settingsState.renderIsExternal)
        return modified
    }

    @Throws(ConfigurationException::class)
    override fun apply() {
        val settingsState: SettingsState = SettingsState.instance
        settingsState.renderMode = settingComponent!!.renderModeText
        settingsState.serverRendering = settingComponent!!.renderServerText
        settingsState.renderIsExternal = settingComponent!!.isExternalRenderBool
    }

    override fun reset() {
        val settingsState: SettingsState = SettingsState.instance
        settingComponent?.renderModeText = settingsState.renderMode
        settingComponent?.renderServerText = settingsState.serverRendering
        settingComponent?.isExternalRenderBool = settingsState.renderIsExternal
    }

    override fun disposeUIResources() {
        settingComponent = null
    }
}