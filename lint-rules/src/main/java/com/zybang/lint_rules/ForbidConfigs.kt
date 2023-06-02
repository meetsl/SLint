package com.zybang.lint_rules

import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Scope
import com.zybang.lint_rules.method.IMethod
import com.zybang.lint_rules.reference.IReference
import com.zybang.lint_rules.reference.VersionCodeReference

/**
 * Created on 2023/6/2 3:29 PM
 * @author shilong
 *
 * desc: 禁用配置名单
 */
object ForbidConfigs {
    const val CONFIG_PACKAGE = "package"
    const val DEFAULT_PACKAGE = "unknown"

    val REF_IMPLEMENTATION = Implementation(ForbidReferenceDetector::class.java, Scope.JAVA_FILE_SCOPE)

    /**
     * 禁用的引用
     */
    val references by lazy {
        mutableListOf<IReference>(
            VersionCodeReference()
        )
    }

    /**
     * 禁用的方法
     */
    val methods by lazy {
        mutableListOf<IMethod>()
    }

    fun getAllForbid(): List<IForbid> {
        return references + methods
    }
}