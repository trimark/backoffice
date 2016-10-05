package com.trimark.backoffice.service;

import java.util.List;
import org.hibernate.criterion.Order;

import com.trimark.backoffice.framework.data.BaseService;
import com.trimark.backoffice.model.entity.Organization;

public interface OrganizationService extends BaseService<Organization, Long> {

    public boolean isOrganizationPresent(String organizationname);
    public List<Organization> getOrganizations(int pageNum, int countPerPage, Order order);

}
