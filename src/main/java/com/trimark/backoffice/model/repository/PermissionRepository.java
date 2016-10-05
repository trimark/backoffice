package com.trimark.backoffice.model.repository;

import com.trimark.backoffice.framework.data.BaseJPARepository;
import com.trimark.backoffice.model.entity.Permission;

public interface PermissionRepository extends BaseJPARepository<Permission, Long> {

    public Permission findByPermissionname(String permissionname);
}

