package com.trimark.backoffice.service;

import java.util.List;
import org.hibernate.criterion.Order;

import com.trimark.backoffice.framework.data.BaseService;
import com.trimark.backoffice.model.entity.Role;

public interface RoleService extends BaseService<Role, Long> {

    public boolean isRolePresent(String rolename);
    public Role findByRolename(String rolename);
    public List<Role> getRoles(int pageNum, int countPerPage, Order order);

}
