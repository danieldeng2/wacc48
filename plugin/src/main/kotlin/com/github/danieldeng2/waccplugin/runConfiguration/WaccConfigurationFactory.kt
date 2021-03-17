package com.github.danieldeng2.waccplugin.runConfiguration

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.components.BaseState
import com.intellij.openapi.project.Project

class WaccConfigurationFactory(type: ConfigurationType) : ConfigurationFactory(type) {
    override fun createTemplateConfiguration(project: Project): RunConfiguration =
        WaccRunConfiguration(project, this, "WACC")

    override fun getName(): String = FACTORY_NAME

    override fun getOptionsClass(): Class<out BaseState> = WaccRunConfigurationOptions::class.java

    companion object {
        private const val FACTORY_NAME = "WACC configuration factory"
    }
}
