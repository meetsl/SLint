package com.zybang.lint_rules.method

import com.android.tools.lint.detector.api.JavaContext
import com.intellij.psi.PsiMethod
import com.zybang.lint_rules.IForbid
import org.jetbrains.uast.UCallExpression

/**
 * Created on 2023/6/2 5:51 PM
 * @author shilong
 *
 * desc: 方法处理抽象
 */
interface IMethod : IForbid {
    /**
     * 对扫描到的元素处理，上报异常
     */
    fun visit(context: JavaContext, node: UCallExpression, method: PsiMethod)
}