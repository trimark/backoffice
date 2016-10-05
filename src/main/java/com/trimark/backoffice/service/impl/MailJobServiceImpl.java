package com.trimark.backoffice.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trimark.backoffice.framework.data.BaseJPAServiceImpl;
import com.trimark.backoffice.model.entity.Job;
import com.trimark.backoffice.model.entity.User;
import com.trimark.backoffice.model.repository.JobRepository;
import com.trimark.backoffice.service.MailJobService;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

@Service
@Transactional
public class MailJobServiceImpl extends BaseJPAServiceImpl<Job, Long> implements MailJobService {
    private static Logger LOG = LoggerFactory.getLogger(MailJobServiceImpl.class);

    protected @Autowired
    JobRepository jobRepository;

    @PostConstruct
    public void setupService() {
        LOG.info("setting up mailJobService...");
        this.baseJpaRepository = jobRepository;
        this.entityClass = Job.class;
        this.baseJpaRepository.setupEntityClass(Job.class);
        LOG.info("mailJobService created...");
    }

    @Override
    public void sendConfirmationMail(User user) {

    }
}
