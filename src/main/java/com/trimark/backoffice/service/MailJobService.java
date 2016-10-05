package com.trimark.backoffice.service;

import com.trimark.backoffice.framework.data.BaseService;
import com.trimark.backoffice.model.entity.Job;
import com.trimark.backoffice.model.entity.User;

public interface MailJobService extends BaseService<Job, Long> {

    /**
     * Sends the confirmation mail to user.
     *
     * @param user
     */
    public void sendConfirmationMail(User user);
}
