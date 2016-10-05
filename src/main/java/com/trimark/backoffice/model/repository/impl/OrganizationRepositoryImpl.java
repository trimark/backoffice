package com.trimark.backoffice.model.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.trimark.backoffice.framework.data.BaseHibernateJPARepository;
import com.trimark.backoffice.model.entity.Organization;
import com.trimark.backoffice.model.repository.OrganizationRepository;

import javax.annotation.PostConstruct;

@Repository
public class OrganizationRepositoryImpl extends BaseHibernateJPARepository<Organization, Long> implements OrganizationRepository {
    private static Logger LOG = LoggerFactory.getLogger(OrganizationRepositoryImpl.class);

    @PostConstruct
    public void setUp() {
        LOG.info("organizationRepository created..!");
    }

    @Override
    public Organization findByOrganizationname(String organizationname) {
        return (Organization) sessionFactory.getCurrentSession().createQuery("from Organization c where c.organizationname = :organizationname")
                .setParameter("organizationname", organizationname).uniqueResult();
    }

}
