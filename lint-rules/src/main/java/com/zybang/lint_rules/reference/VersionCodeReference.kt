package com.zybang.lint_rules.reference

import com.android.tools.lint.detector.api.*
import com.intellij.psi.PsiField
import com.zybang.lint_rules.ForbidConfigs
import com.zybang.lint_rules.IssueUtil.extConfig
import org.jetbrains.uast.UReferenceExpression

/**
 * Created on 2023/6/2 3:17 PM
 * @author shilong
 *
 * desc: BuildConfig.VERSION_CODE 扫描并禁用
 */
class VersionCodeReference : BaseReference() {
    companion object {
        private const val MESSAGE = "Using `%1\$s.%2\$s` to get app version code is not recommended"

        @JvmStatic
        val ISSUE = Issue.create(
            id = "BuildVersionCode",
            briefDescription = "BuildConfig.VERSION_CODE Usage",
            explanation = "Using BuildConfig.VERSION_CODE is not recommended",
            category = Category.CORRECTNESS,
            priority = 6,
            severity = Severity.ERROR,
            implementation = ForbidConfigs.REF_IMPLEMENTATION
        ).extConfig()
    }

    override fun getNames(): List<String> {
        return listOf("VERSION_CODE")
    }

    override fun getIssue(): Issue {
        return ISSUE
    }

    override fun validVisit(context: JavaContext, expression: UReferenceExpression, psiField: PsiField) {
        val className = psiField.containingClass?.name ?: ""
        if ("BuildConfig" == className) {
            val message = String.format(MESSAGE, className, getNames())
            context.report(ISSUE, expression, context.getNameLocation(expression), message)
        }
    }
}