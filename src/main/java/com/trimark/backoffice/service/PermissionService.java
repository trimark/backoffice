package com.trimark.backoffice.service;

import java.util.List;
import org.hibernate.criterion.Order;

import com.trimark.backoffice.framework.data.BaseService;
import com.trimark.backoffice.model.entity.Permission;

public interface PermissionService extends BaseService<Permission, Long> {

    public boolean isPermissionPresent(String permissionname);
    public List<Permission> getPermissions(int pageNum, int countPerPage, Order order);

}
