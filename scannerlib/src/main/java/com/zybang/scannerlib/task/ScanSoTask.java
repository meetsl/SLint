package com.zybang.scannerlib.task;

import com.android.build.gradle.internal.profile.AnalyticsService;
import com.android.build.gradle.internal.tasks.NonIncrementalTask;

import org.gradle.api.provider.Property;
import org.gradle.workers.WorkerExecutor;

/**
 * Created on 2023/5/19 3:53 PM
 *
 * @author shilong
 * <p>
 * desc: 扫描so文件任务
 */
public class ScanSoTask extends NonIncrementalTask {
    @Override
    public Property<AnalyticsService> getAnalyticsService() {
        return null;
    }

    @Override
    public Property<String> getProjectPath() {
        return null;
    }

    @Override
    public WorkerExecutor getWorkerExecutor() {
        return null;
    }

    @Override
    protected void doTaskAction() throws Exception {

    }
}
