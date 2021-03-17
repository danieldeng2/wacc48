package com.github.danieldeng2.waccplugin.runConfiguration

import com.github.danieldeng2.waccplugin.language.WaccFileType
import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.LazyRunConfigurationProducer
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement

class WaccRunConfigurationProducer : LazyRunConfigurationProducer<WaccRunConfiguration>() {

    override fun getConfigurationFactory(): ConfigurationFactory =
        WaccConfigurationFactory(WaccRunConfigurationType)

    override fun isConfigurationFromContext(
        configuration: WaccRunConfiguration,
        context: ConfigurationContext
    ): Boolean {
        if (context.psiLocation?.containingFile?.fileType == WaccFileType) {
            if (context.psiLocation?.containingFile?.virtualFile?.canonicalPath == configuration.waccFileName) {
                return true
            }
        }
        return false
    }

    override fun setupConfigurationFromContext(
        configuration: WaccRunConfiguration,
        context: ConfigurationContext,
        sourceElement: Ref<PsiElement>
    ): Boolean {
        if (context.psiLocation?.containingFile?.fileType == WaccFileType) {
            val file = context.psiLocation?.containingFile?.virtualFile
            if (file != null && configuration.waccFileName != file.canonicalPath) {
                configuration.waccFileName = file.canonicalPath
                configuration.name = file.name
                return true
            }
        }
        return false
    }
}
