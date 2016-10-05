package com.trimark.backoffice.model.repository;

import com.trimark.backoffice.framework.data.BaseJPARepository;
import com.trimark.backoffice.model.entity.User;

/**
 *
 * DD Repository for User related actions and events
 *
 */
public interface UserRepository extends BaseJPARepository<User, Long> {
    /**
     * Finds a user with the given email
     *
     * @param email
     * @return
     */
    public User findByEmail(String email);
}
