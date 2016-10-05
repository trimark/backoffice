package com.trimark.backoffice.service.impl;

import java.util.List;
import javax.annotation.PostConstruct;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trimark.backoffice.framework.data.BaseJPAServiceImpl;
import com.trimark.backoffice.model.entity.Role;
import com.trimark.backoffice.model.repository.RoleRepository;
import com.trimark.backoffice.service.RoleService;

@Service
@Transactional
public class RoleServiceImpl extends BaseJPAServiceImpl<Role, Long> implements RoleService {
    private @Autowired RoleRepository roleRepository;
    
    @PostConstruct
    public void setupService() {
        this.baseJpaRepository = roleRepository;
        this.entityClass = Role.class;
        this.baseJpaRepository.setupEntityClass(Role.class);
    }

    @Override
    public boolean isRolePresent(String rolename) {
        if (roleRepository.findByRolename(rolename) != null) {
            return true;
        } else
            return false;
    }
    
    @Override
    public Role findByRolename(String rolename) {
        return roleRepository.findByRolename(rolename);
    }
    
    @Override
    public List<Role> getRoles(int pageNum, int countPerPage, Order order) {
    	List<Role> roles = (List<Role>) roleRepository.findAllByPage(pageNum, countPerPage, order);
        return roles;
    }
}
