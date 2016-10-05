package com.trimark.backoffice.model.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.trimark.backoffice.framework.data.BaseHibernateJPARepository;
import com.trimark.backoffice.model.entity.Permission;
import com.trimark.backoffice.model.repository.PermissionRepository;

import javax.annotation.PostConstruct;

@Repository
public class PermissionRepositoryImpl extends BaseHibernateJPARepository<Permission, Long> implements PermissionRepository {
    private static Logger LOG = LoggerFactory.getLogger(PermissionRepositoryImpl.class);

    @PostConstruct
    public void setUp() {
        LOG.info("permissionRepository created..!");
    }

    @Override
    public Permission findByPermissionname(String permissionname) {
        return (Permission) sessionFactory.getCurrentSession().createQuery("from Permission c where c.permissionname = :permissionname")
                .setParameter("permissionname", permissionname).uniqueResult();
    }

}
