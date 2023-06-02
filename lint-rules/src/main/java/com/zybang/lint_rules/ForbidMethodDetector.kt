package com.zybang.lint_rules

import com.android.tools.lint.detector.api.*
import com.intellij.psi.PsiMethod
import org.jetbrains.uast.UCallExpression

/**
 * Created on 2023/5/23 8:09 PM
 * @author shilong
 *
 * desc: Detect forbidden methods.
 *
 * ref: HardwareIdDetector
 */
open class ForbidMethodDetector : Detector(), SourceCodeScanner {

    override fun getApplicableMethodNames(): List<String>? {
        return getAllMethodNames()
    }

    override fun visitMethodCall(context: JavaContext, node: UCallExpression, method: PsiMethod) {
        ForbidConfigs.methods.forEach { it.visit(context, node, method) }
    }

    private fun getAllMethodNames(): List<String> {
        val result = mutableListOf<String>()
        for (method in ForbidConfigs.methods) {
            result.addAll(method.getNames())
        }
        return result
    }
}