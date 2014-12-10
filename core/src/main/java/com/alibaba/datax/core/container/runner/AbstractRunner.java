package com.alibaba.datax.core.container.runner;

import com.alibaba.datax.common.plugin.AbstractTaskPlugin;
import com.alibaba.datax.common.plugin.TaskPluginCollector;
import com.alibaba.datax.common.util.Configuration;
import com.alibaba.datax.core.statistics.communication.Communication;
import com.alibaba.datax.service.face.domain.State;
import org.apache.commons.lang.Validate;

/**
 * Created by jingxing on 14-9-1.
 */
public abstract class AbstractRunner {
    private AbstractTaskPlugin plugin;

    private Configuration jobConf;

    private Communication runnerCommunication;

    private int taskGroupId;

    private int taskId;

    public AbstractRunner(AbstractTaskPlugin abstractTaskPlugin) {
        this.plugin = abstractTaskPlugin;
    }

    public void destroy() {
        if (this.plugin != null) {
            this.plugin.destroy();
            this.plugin = null;
        }
    }

    public State getRunnerState() {
        return this.runnerCommunication.getState();
    }

    public AbstractTaskPlugin getPlugin() {
        return plugin;
    }

    public void setPlugin(AbstractTaskPlugin plugin) {
        this.plugin = plugin;
    }

    public Configuration getJobConf() {
        return jobConf;
    }

    public void setJobConf(Configuration jobConf) {
        this.jobConf = jobConf;
        this.plugin.setPluginJobConf(jobConf);
    }

    public void setTaskPluginCollector(TaskPluginCollector pluginCollector) {
        this.plugin.setTaskPluginCollector(pluginCollector);
    }

    private void mark(State state) {
        this.runnerCommunication.setState(state);
    }

    public void markRun() {
        mark(State.RUNNING);
    }

    public void markSuccess() {
        mark(State.SUCCEEDED);
    }

    public void markFail(final Throwable throwable) {
        mark(State.FAILED);

        this.runnerCommunication.setThrowable(throwable);
    }

    /**
     * @param taskGroupId the taskGroupId to set
     */
    public void setTaskGroupId(int taskGroupId) {
        this.taskGroupId = taskGroupId;
    }

    /**
     * @return the taskGroupId
     */
    public int getTaskGroupId() {
        return taskGroupId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public void setRunnerCommunication(final Communication runnerCommunication) {
        Validate.notNull(runnerCommunication,
                "插件的Communication不能为空");
        this.runnerCommunication = runnerCommunication;
    }
}
