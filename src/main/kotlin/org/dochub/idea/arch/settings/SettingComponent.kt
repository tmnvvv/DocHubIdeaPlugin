package org.dochub.idea.arch.settings

import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import java.util.*
import javax.swing.DefaultComboBoxModel
import javax.swing.JComponent
import javax.swing.JPanel

class SettingComponent {

    val myMainPanel: JPanel
    private val renderModeModel = DefaultComboBoxModel<String>()
    private val renderMode = ComboBox(renderModeModel)
    private val renderServer = JBTextField()
    private val isExternalRender = JBCheckBox("External rendering")

    init {
        renderModeModel.addAll(Arrays.asList(*SettingsState.renderModes))
        isExternalRender.addItemListener { updateVisible() }
        myMainPanel = FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel("Render mode: "), renderMode, 1, false)
            .addComponent(isExternalRender, 1)
            .addLabeledComponent(JBLabel("Render server: "), renderServer, 1, false)
            .addComponentFillVertically(JPanel(), 0)
            .panel
        updateVisible()
    }

    private fun updateVisible() {
        renderServer.isEnabled = isExternalRender.isSelected
    }

    val preferredFocusedComponent: JComponent
        get() = renderMode
    var renderModeText: String
        get() = renderModeModel.getElementAt(renderMode.selectedIndex)
        set(mode) {
            val index = renderModeModel.getIndexOf(mode)
            if (index >= 0) renderMode.selectedIndex = index
            updateVisible()
        }
    var isExternalRenderBool: Boolean
        get() = isExternalRender.isSelected
        set(value) {
            isExternalRender.isSelected = value
            updateVisible()
        }
    var renderServerText: String
        get() = renderServer.text
        set(server) {
            renderServer.text = server
            updateVisible()
        }
}
