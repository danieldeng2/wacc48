package com.github.danieldeng2.waccplugin.runConfiguration

import com.github.danieldeng2.waccplugin.language.WaccIcons
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import javax.swing.Icon

object WaccRunConfigurationType : ConfigurationType {
    override fun getDisplayName(): String = "WACC Application"

    override fun getConfigurationTypeDescription(): String = "Wacc Run Configuration Type"

    override fun getIcon(): Icon = WaccIcons.FILE

    override fun getId(): String = "WaccRunConfiguration"

    override fun getConfigurationFactories(): Array<ConfigurationFactory> =
        arrayOf(WaccConfigurationFactory(this))
}
