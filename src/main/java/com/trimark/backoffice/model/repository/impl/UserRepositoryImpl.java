package com.trimark.backoffice.model.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.trimark.backoffice.framework.data.BaseHibernateJPARepository;
import com.trimark.backoffice.model.entity.User;
import com.trimark.backoffice.model.repository.UserRepository;

/**
 *
 * User Repository Implementation
 *
 */
@Repository
public class UserRepositoryImpl extends BaseHibernateJPARepository<User, Long> implements UserRepository {
    private static Logger LOG = LoggerFactory.getLogger(UserRepositoryImpl.class);

    @Override
    public User findByEmail(String email) {
        return (User) sessionFactory.getCurrentSession().createQuery("from User u where u.email = :email")
                .setParameter("email", email).uniqueResult();
    }
}
