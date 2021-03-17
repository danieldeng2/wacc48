package com.github.danieldeng2.waccplugin.completion

import com.intellij.codeInsight.template.TemplateActionContext
import com.intellij.codeInsight.template.TemplateContextType

class WaccContext : TemplateContextType("WACC", "WACC") {
    override fun isInContext(templateActionContext: TemplateActionContext): Boolean {
        return templateActionContext.file.name.endsWith(".wacc")
    }
}
