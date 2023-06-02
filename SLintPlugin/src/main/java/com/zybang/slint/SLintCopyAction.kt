package com.zybang.slint

import org.gradle.api.Action
import org.gradle.api.GradleException
import org.gradle.api.Task
import java.io.*

/**
 * Created on 2023/6/1 7:23 PM
 * @author shilong
 *
 * desc: 将 plugin lint.xml 拷贝到 app 下生效
 */
class SLintCopyAction : Action<Task> {
    override fun execute(task: Task) {
        val project = task.project
        println("==========================  lintAnalyzeTask copy start =============================================")
        //Lint 会把project下的lint.xml和lintConfig指定的lint.xml进行合并
        val lintFile: File = project.file(SLintParams.LINT_FILE_NAME)
        var lintOldFile: File? = null
        //如果 lint.xml 存在，则改名为 lint-origin.xml
        if (lintFile.exists()) {
            lintOldFile = project.file(SLintParams.LINT_ORIGIN_FILE_NAME)
            lintFile.renameTo(lintOldFile)
        }
        //进行 将plugin内置的lint.xml文件拷贝到项目下面的lint.xml
        val isLintXmlReady = copyLintXml(lintFile)
        //拷贝失败，lint-origin.xml 文件改名为 lint.xml
        if (!isLintXmlReady) {
            lintOldFile?.renameTo(lintFile)
            throw GradleException("plugin copy resource lint.xml to project failure")
        }
        println("==========================  lintAnalyzeTask copy end =============================================")
    }

    /**
     * 复制 lint.xml 到 targetFile
     * @param targetFile 复制到的目标文件
     * @return 是否复制成功
     */
    private fun copyLintXml(targetFile: File): Boolean {
        //创建目录
        targetFile.parentFile.mkdirs()

        //目标文件为  resources/config/lint.xml文件
        val lintIns: InputStream? = this::class.java.getResourceAsStream("/config/lint.xml")
        val outputStream: OutputStream = FileOutputStream(targetFile)

        copy(lintIns, outputStream)
        closeQuietly(outputStream)
        closeQuietly(lintIns)

        //如果复制操作完成后，目标文件存在
        if (targetFile.exists()) {
            return true
        }
        return false
    }

    private fun copy(inputStream: InputStream?, outputStream: OutputStream?) {
        if (inputStream == null || outputStream == null) {
            return
        }
        val buffer = ByteArray(8192)
        var n: Int
        while (-1 != inputStream.read(buffer).also { n = it }) {
            outputStream.write(buffer, 0, n)
        }
    }

    private fun closeQuietly(closeable: Closeable?) {
        if (closeable != null) {
            try {
                closeable.close()
            } catch (ignored: IOException) {
            }
        }
    }
}