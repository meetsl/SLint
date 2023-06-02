package com.zybang.lint_rules.reference

import com.android.tools.lint.detector.api.JavaContext
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiField
import com.zybang.lint_rules.IssueUtil.isValidPackage
import org.jetbrains.uast.UReferenceExpression

/**
 * Created on 2023/6/2 3:56 PM
 * @author shilong
 *
 * desc: 引用基类
 */
abstract class BaseReference : IReference {

    override fun visit(context: JavaContext, expression: UReferenceExpression, psiElement: PsiElement) {
        //对检查引用的包名进行校验
        if (psiElement is PsiField && getNames().contains(psiElement.name) && this.isValidPackage(context, psiElement)) {
            validVisit(context, expression, psiElement)
        }
    }

    abstract fun validVisit(context: JavaContext, expression: UReferenceExpression, psiField: PsiField)
}