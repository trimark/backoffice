package com.trimark.backoffice.model.repository;

import com.trimark.backoffice.framework.data.BaseJPARepository;
import com.trimark.backoffice.model.entity.Role;

public interface RoleRepository extends BaseJPARepository<Role, Long> {

    public Role findByRolename(String rolename);
}

