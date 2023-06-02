package com.zybang.lint_rules

import com.android.tools.lint.detector.api.*
import com.intellij.psi.PsiElement
import org.jetbrains.uast.UReferenceExpression

/**
 * Created on 2023/5/23 8:09 PM
 * @author shilong
 *
 * desc: Detect forbidden references.
 *
 * ref: HardwareIdDetector
 */
open class ForbidReferenceDetector : Detector(), SourceCodeScanner {


    override fun getApplicableReferenceNames(): List<String> {
        return getAllReferenceNames()
    }

    override fun visitReference(context: JavaContext, reference: UReferenceExpression, referenced: PsiElement) {
        ForbidConfigs.references.forEach { it.visit(context, reference, referenced) }
    }

    private fun getAllReferenceNames(): List<String> {
        val result = mutableListOf<String>()
        for (reference in ForbidConfigs.references) {
            result.addAll(reference.getNames())
        }
        return result
    }
}