package com.zybang.lint_rules

import com.android.tools.lint.client.api.IssueRegistry
import com.android.tools.lint.client.api.Vendor
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
        get() = ForbidConfigs.getAllForbid().map { it.getIssue() }

    override val api: Int
        get() = CURRENT_API

    override val vendor: Vendor
        get() = Vendor(
            vendorName = "SLint",
            feedbackUrl = "https://github.com/meetsl/SLint",
            contact = "meetsl.dev@gmail.com"
        )
}