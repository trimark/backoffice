package com.trimark.backoffice.model.repository;

import java.util.List;

import com.trimark.backoffice.framework.data.BaseJPARepository;
import com.trimark.backoffice.model.entity.Job;

public interface JobRepository extends BaseJPARepository<Job, Long> {
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
