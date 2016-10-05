package com.trimark.backoffice.model.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.trimark.backoffice.framework.data.BaseHibernateJPARepository;
import com.trimark.backoffice.model.entity.Role;
import com.trimark.backoffice.model.repository.RoleRepository;

import javax.annotation.PostConstruct;

@Repository
public class RoleRepositoryImpl extends BaseHibernateJPARepository<Role, Long> implements RoleRepository {
    private static Logger LOG = LoggerFactory.getLogger(RoleRepositoryImpl.class);

    @PostConstruct
    public void setUp() {
        LOG.info("roleRepository created..!");
    }

    @Override
    public Role findByRolename(String rolename) {
        return (Role) sessionFactory.getCurrentSession().createQuery("from Role c where c.rolename = :rolename")
                .setParameter("rolename", rolename).uniqueResult();
    }

}
