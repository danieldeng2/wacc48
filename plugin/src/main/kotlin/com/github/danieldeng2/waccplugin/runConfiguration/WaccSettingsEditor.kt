package com.github.danieldeng2.waccplugin.runConfiguration

import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.ui.LabeledComponent
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import javax.swing.JComponent
import javax.swing.JPanel

class WaccSettingsEditor : SettingsEditor<WaccRunConfiguration?>() {
    private lateinit var myPanel: JPanel
    private lateinit var waccFileName: LabeledComponent<TextFieldWithBrowseButton>

    override fun createEditor(): JComponent = myPanel

    override fun resetEditorFrom(s: WaccRunConfiguration) {
        if (s.waccFileName != null) {
            waccFileName.component.text = s.waccFileName!!
        }
    }

    override fun applyEditorTo(s: WaccRunConfiguration) {
        s.waccFileName = waccFileName.component.text
    }

    private fun createUIComponents() {
        waccFileName = LabeledComponent<TextFieldWithBrowseButton>()
        waccFileName.component = TextFieldWithBrowseButton()

        waccFileName.component.addBrowseFolderListener(
            "Choose a wacc file",
            null,
            null,
            FileChooserDescriptorFactory.createSingleFileDescriptor("wacc")
        )
    }
}
