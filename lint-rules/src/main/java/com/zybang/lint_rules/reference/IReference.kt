package com.zybang.lint_rules.reference

import com.android.tools.lint.detector.api.JavaContext
import com.intellij.psi.PsiElement
import com.zybang.lint_rules.IForbid
import org.jetbrains.uast.UReferenceExpression

/**
 * Created on 2023/6/2 3:05 PM
 * @author shilong
 *
 * desc: 引用处理抽象类
 */
interface IReference : IForbid {

    /**
     * 对引用的元素进行处理
     */
    fun visit(context: JavaContext, expression: UReferenceExpression, psiElement: PsiElement)
}