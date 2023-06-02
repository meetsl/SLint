@file:Suppress("DEPRECATION")

package com.zybang.slint

import com.android.build.api.dsl.*
import com.android.build.gradle.*
import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.api.BaseVariant
import com.android.build.gradle.internal.dependency.VariantDependencies
import org.gradle.api.*
import org.gradle.api.artifacts.dsl.DependencyHandler
import java.io.*

/**
 * Created on 2023/5/19 3:47 PM
 *
 * @author shilong
 *
 *
 * desc: 扫描插件
 */
@Suppress("UnstableApiUsage")
class SLintPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        applyTask(project, getAndroidVariants(project))
    }

    /**
     *  插件的实际应用：统一管理lint.xml和lintOptions，自动添加aar。
     * @param project 项目
     * @param variants 项目的variants
     */
    private fun applyTask(project: Project, variants: DomainObjectSet<out BaseVariant>?) {
        //========================== 统一  自动添加AAR  开始=============================================//
        //配置project的dependencies配置，默认都自动加上 自定义lint检测的AAR包
        val dependencyHandler: DependencyHandler = project.dependencies
        if (project.plugins.hasPlugin(AppPlugin::class.java)) {
            //如果是android application项目
            dependencyHandler.add(VariantDependencies.CONFIG_NAME_IMPLEMENTATION, dependencyHandler.create(SLintParams.SLINT_DEPENDENCY))
        } else {
            dependencyHandler.add(VariantDependencies.CONFIG_NAME_COMPILE_ONLY, dependencyHandler.create(SLintParams.SLINT_DEPENDENCY))
        }

        //去除gradle缓存的配置
        project.configurations.all {
            it.resolutionStrategy.cacheChangingModulesFor(0, "seconds")
            it.resolutionStrategy.cacheDynamicVersionsFor(0, "seconds")
        }

        //========================== 统一  自动添加AAR  结束=============================================//

        //==========================  统一  lintOptions  开始=============================================//
        val commonExtension = getExtension(project)
        commonExtension?.lint?.apply {
            this.checkOnly.clear()
            this.checkOnly.add(SLintParams.BUILD_VERSION_CODE_ISSUE)
            // 配置lintConfig的配置文件路径
            this.lintConfig = project.file(SLintParams.LINT_FILE_NAME)
            // 是否将所有的warnings视为errors
            // this.warningsAsErrors = true
            // 是否lint检测出错则停止编译
            this.abortOnError = true
            // htmlReport打开
            this.htmlReport = true
            this.htmlOutput = project.file("${project.buildDir}/reports/lint/lint-result.html")
            // xmlReport打开 因为Jenkins上的插件需要xml文件
            this.xmlReport = true
            this.xmlOutput = project.file("${project.buildDir}/reports/lint/lint-result.xml")
        }
        //==========================  统一  lintOptions  结束=============================================//

        variants?.all { variant ->
            //获取Lint Task
            val variantName = variant.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
            val lintAnalyzeTask = project.tasks.getByName("lintAnalyze$variantName")
            val lintTask = project.tasks.getByName("lint$variantName")
            //==========================  统一  lint.xml  开始=============================================//
            // lintTask.doFirst 执行太晚，选择 lintAnalyzeTask
            lintAnalyzeTask.doFirst(SLintCopyAction())
            //lint任务执行后，删除lint.xml
            project.gradle.taskGraph.afterTask {
                if (it == lintTask) {
                    println("==========================  lintTask afterTask =============================================")
                    val delLintFile = project.file(SLintParams.LINT_FILE_NAME)
                    if (delLintFile.exists()) {
                        delLintFile.delete()
                    }
                    val originLintFile = project.file(SLintParams.LINT_ORIGIN_FILE_NAME)
                    if (originLintFile.exists()) {
                        originLintFile.renameTo(delLintFile)
                    }
                }
            }
            //==========================  统一  lint.xml  结束=============================================//

            //==========================  在终端 执行命令 gradlew lintForXTC  的配置  开始=============================================//
            // 在终端 执行命令 gradlew lintForXTC 的时候，则会应用  lintTask
            //创建一个task 名为  lintForXXX
            val taskProvider = project.tasks.register("lintFor$variantName") {
                it.group = SLintParams.TASK_GROUP
                it.dependsOn(lintTask)
            }
            val assembleTask = project.tasks.findByName("assemble$variantName")
            assembleTask?.dependsOn(taskProvider)
            //==========================  在终端 执行命令 gradlew lintForXTC  的配置  结束=============================================//
        }
    }

    /**
     * 获取project 项目 中 android项目 或者library项目 的 variant 列表
     * @param project 要编译的项目
     * @return variants 列表
     */
    private fun getAndroidVariants(project: Project): DomainObjectSet<out BaseVariant>? {
        if (project.plugins.hasPlugin(AppPlugin::class.java)) {
            val appExtension = project.extensions.findByType(AppExtension::class.java)
            return appExtension?.applicationVariants
        } else if (project.plugins.hasPlugin(LibraryPlugin::class.java)) {
            val libraryExtension = project.extensions.findByType(LibraryExtension::class.java)
            return libraryExtension?.libraryVariants
        }
        throw ProjectConfigurationException(SLintParams.MISS_CONFIGURED_ERROR_MESSAGE, Throwable())
    }

    private fun getExtension(project: Project): CommonExtension<out BuildFeatures, out BuildType, out DefaultConfig, out ProductFlavor>? {
        if (project.plugins.hasPlugin(AppPlugin::class.java)) {
            return project.extensions.findByType(ApplicationExtension::class.java)
        } else if (project.plugins.hasPlugin(LibraryPlugin::class.java)) {
            return project.extensions.findByType(LibraryExtension::class.java)
        }
        throw ProjectConfigurationException(SLintParams.MISS_CONFIGURED_ERROR_MESSAGE, Throwable())
    }
}