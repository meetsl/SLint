package com.zybang.lint_rules

import com.android.tools.lint.detector.api.Issue

/**
 * Created on 2023/6/2 5:44 PM
 * @author shilong
 *
 * desc: 公共接口
 */
interface IForbid {
    /**
     * 要检查的引用/方法称
     */
    fun getNames(): List<String>

    /**
     * 获取Issue
     */
    fun getIssue(): Issue
}