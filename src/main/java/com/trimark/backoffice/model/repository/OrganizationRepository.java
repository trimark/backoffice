package com.trimark.backoffice.model.repository;

import com.trimark.backoffice.framework.data.BaseJPARepository;
import com.trimark.backoffice.model.entity.Organization;

public interface OrganizationRepository extends BaseJPARepository<Organization, Long> {

    public Organization findByOrganizationname(String organizationname);
}

