package com.trimark.backoffice.service;

import java.util.List;

import com.trimark.backoffice.framework.data.BaseService;
import com.trimark.backoffice.model.entity.Job;

public interface JobService extends BaseService<Job, Long> {
    /**
     *
     * @param count
     * @return
     */
    public List<Job> fetchNewJobsToBeScheduledForExecutionPerPriority(int count);

    /**
     *
     * @param count
     * @return
     */
    public List<Job> fetchFailedJobsToBeScheduledForExecutionPerPriority(int count);

    /**
     *
     * @param count
     * @return
     */
    public List<Job> fetchNewJobsToBeScheduledForExecutionPerSubmissionTimePriority(int count);

    /**
     *
     * @param count
     * @return
     */
    public List<Job> fetchFailedJobsToBeScheduledForExecutionPerSubmissionTimePriority(int count);
}