package com.zybang.lint_rules

import com.android.tools.lint.detector.api.*
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiField
import org.jetbrains.uast.UReferenceExpression

/**
 * Created on 2023/5/23 8:09 PM
 * @author shilong
 *
 * desc: Detect BuildConfig.VERSION_CODE calls.
 *
 * ref: HardwareIdDetector
 */
@Suppress("UnstableApiUsage")
open class VersionCodeDetector : Detector(), SourceCodeScanner {
    companion object {
        private const val CONFIG_PACKAGE = "package"
        private const val DEFAULT_PACKAGE = "unknown"
        private const val MESSAGE_VERSION_CODE = "Using `%1\$s` to get app version code is not recommended"

        private val IMPLEMENTATION = Implementation(VersionCodeDetector::class.java, Scope.JAVA_FILE_SCOPE)


        @JvmStatic
        val ISSUE = Issue.create(
            id = "BuildVersionCode",
            briefDescription = "BuildConfig.VERSION_CODE Usage",
            explanation = "Using BuildConfig.VERSION_CODE is not recommended",
            category = Category.CORRECTNESS,
            priority = 6,
            severity = Severity.ERROR,
            implementation = IMPLEMENTATION
        )
            .setAndroidSpecific(true)
            .setOptions(listOf(StringOption("package", "scan package", DEFAULT_PACKAGE)))
    }

    override fun getApplicableReferenceNames(): List<String> {
        return listOf("VERSION_CODE")
    }

    override fun visitReference(context: JavaContext, reference: UReferenceExpression, referenced: PsiElement) {
        val evaluator = context.evaluator
        val configPackage = OptionUtil.getOption<String>(context, CONFIG_PACKAGE)
        val packageName = evaluator.getPackage(referenced)?.qualifiedName
        if (referenced is PsiField && (configPackage == DEFAULT_PACKAGE || configPackage == packageName)) {
            val className = referenced.containingClass?.name ?: ""
            if ("BuildConfig" == className) {
                val message = String.format(MESSAGE_VERSION_CODE, "VERSION_CODE")
                context.report(ISSUE, reference, context.getNameLocation(reference), message)
            }
        }
    }
}