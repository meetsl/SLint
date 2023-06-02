package com.zybang.lint_rules

import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.StringOption
import com.intellij.psi.PsiElement

/**
 * Created on 2023/5/24 5:05 PM
 * @author shilong
 *
 * desc: 获取 lint 对应的自定义配置参数
 */
object IssueUtil {
    @Suppress("UNCHECKED_CAST")
    fun <T> Issue.getOption(context: JavaContext, name: String): T? {
        val options = this.getOptions()
        options.forEach { option ->
            if (option.name == name) {
                return option.getValue(context) as? T
            }
        }
        return null
    }

    fun Issue.extConfig(): Issue {
        return this.setAndroidSpecific(true)
            .setOptions(listOf(StringOption("package", "scan package", ForbidConfigs.DEFAULT_PACKAGE)))
    }

    /**
     * 对检查引用的包名进行校验
     */
    fun IForbid.isValidPackage(context: JavaContext, psiElement: PsiElement): Boolean {
        val evaluator = context.evaluator
        val configPackage = getIssue().getOption<String>(context, ForbidConfigs.CONFIG_PACKAGE)
        val packageName = evaluator.getPackage(psiElement)?.qualifiedName
        println("===== ${getNames()} configPackage is $configPackage ======")
        return configPackage == ForbidConfigs.DEFAULT_PACKAGE || configPackage == packageName
    }
}