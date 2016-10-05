package com.trimark.backoffice.service.impl;

import java.util.List;
import javax.annotation.PostConstruct;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trimark.backoffice.framework.data.BaseJPAServiceImpl;
import com.trimark.backoffice.model.entity.Organization;
import com.trimark.backoffice.model.repository.OrganizationRepository;
import com.trimark.backoffice.service.OrganizationService;

@Service
@Transactional
public class OrganizationServiceImpl extends BaseJPAServiceImpl<Organization, Long> implements OrganizationService {
    private @Autowired OrganizationRepository organizationRepository;
    
    @PostConstruct
    public void setupService() {
        this.baseJpaRepository = organizationRepository;
        this.entityClass = Organization.class;
        this.baseJpaRepository.setupEntityClass(Organization.class);
    }

    @Override
    public boolean isOrganizationPresent(String rolename) {
        if (organizationRepository.findByOrganizationname(rolename) != null) {
            return true;
        } else
            return false;
    }
    
    @Override
    public List<Organization> getOrganizations(int pageNum, int countPerPage, Order order) {
    	List<Organization> organizations = (List<Organization>) organizationRepository.findAllByPage(pageNum, countPerPage, order);
        return organizations;
    }
}
