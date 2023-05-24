package com.zybang.lint_rules

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.detector.api.CURRENT_API
import com.android.tools.lint.detector.api.Issue

/**
 * Created on 2023/5/23 3:22 PM
 *
 * @author shilong
 *
 *
 * desc: 注册类
 */
@Suppress("UnstableApiUsage")
class ZybIssueRegistry : IssueRegistry() {
    override val issues: List<Issue>
        get() = mutableListOf(VersionCodeDetector.ISSUE)

    override val api: Int
        get() = CURRENT_API
}