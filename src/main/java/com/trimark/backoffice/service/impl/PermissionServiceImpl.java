package com.trimark.backoffice.service.impl;

import java.util.List;
import javax.annotation.PostConstruct;

import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.trimark.backoffice.framework.data.BaseJPAServiceImpl;
import com.trimark.backoffice.model.entity.Permission;
import com.trimark.backoffice.model.repository.PermissionRepository;
import com.trimark.backoffice.service.PermissionService;

@Service
@Transactional
public class PermissionServiceImpl extends BaseJPAServiceImpl<Permission, Long> implements PermissionService {
    private @Autowired PermissionRepository permissionRepository;
    
    @PostConstruct
    public void setupService() {
        this.baseJpaRepository = permissionRepository;
        this.entityClass = Permission.class;
        this.baseJpaRepository.setupEntityClass(Permission.class);
    }

    @Override
    public boolean isPermissionPresent(String rolename) {
        if (permissionRepository.findByPermissionname(rolename) != null) {
            return true;
        } else
            return false;
    }
    
    @Override
    public List<Permission> getPermissions(int pageNum, int countPerPage, Order order) {
    	List<Permission> permissions = (List<Permission>) permissionRepository.findAllByPage(pageNum, countPerPage, order);
        return permissions;
    }
}
