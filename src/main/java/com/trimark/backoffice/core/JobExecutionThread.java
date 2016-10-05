package com.trimark.backoffice.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.trimark.backoffice.model.entity.Job;

import java.util.Date;
import java.util.concurrent.Callable;

public class JobExecutionThread implements Callable {
    private static Logger LOG = LoggerFactory.getLogger(JobExecutionThread.class);
    private Job job;

    public JobExecutionThread(Job jobInfo) {
        job = jobInfo;
    }

    @Override
    public Job call() throws Exception {
        LOG.info("Executing the Job: "+job.getName());
        Thread.sleep(10);
        LOG.info("Finished executing the Job: "+job.getName());
        job.setCompletionTime(new Date(System.currentTimeMillis()));
        job.setStatus(Job.Status.SUCCESSFUL);
        return job;
    }
}
