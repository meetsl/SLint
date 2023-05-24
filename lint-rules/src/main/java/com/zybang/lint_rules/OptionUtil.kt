package com.zybang.lint_rules

import com.android.tools.lint.detector.api.JavaContext

/**
 * Created on 2023/5/24 5:05 PM
 * @author shilong
 *
 * desc: 获取 lint 对应的自定义配置参数
 */
object OptionUtil {
    @Suppress("UNCHECKED_CAST")
    fun <T> getOption(context: JavaContext, name: String): T? {
        val options = VersionCodeDetector.ISSUE.getOptions()
        options.forEach { option ->
            if (option.name == name) {
                return option.getValue(context) as? T
            }
        }
        return null
    }
}