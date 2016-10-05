package com.trimark.backoffice.model.entity;

public class MailerJob extends Job {
    enum MailType {
        CONFIRMATION, PASSWORD_RESET
    }

    public static MailerJob buildMailerJobForUser(User user, MailType mailType) {
        MailerJob mailerJob = new MailerJob();
        //mailerJob.set
        return mailerJob;
    }
}
