package com.trimark.backoffice.controller;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapperSingletonWrapper;
import org.dozer.Mapper;
import org.hibernate.criterion.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.trimark.backoffice.framework.controller.BaseController;
import com.trimark.backoffice.model.dto.PermissionDTO;
import com.trimark.backoffice.model.dto.RoleDTO;
import com.trimark.backoffice.model.entity.Organization;
import com.trimark.backoffice.model.entity.Permission;
import com.trimark.backoffice.model.entity.Role;
import com.trimark.backoffice.service.RoleService;

@RestController
@RequestMapping("role")
public class RoleController extends BaseController {
    private static Logger LOG = LoggerFactory.getLogger(RoleController.class);
    private Mapper mapper = DozerBeanMapperSingletonWrapper.getInstance();
    
    @Autowired 
    private RoleService roleService;


    //-------------------Retrieve All Roles--------------------------------------------------------
    
    @RequestMapping(value = "/list/", method = RequestMethod.GET)
    public ResponseEntity<List<RoleDTO>> listAllRoles() throws Exception {
        List<Role> roles = (List<Role>) roleService.getRoles(1, 1000, Order.asc("rolename"));
        if(roles.isEmpty()){
            return new ResponseEntity<List<RoleDTO>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        List<RoleDTO> out = new ArrayList<RoleDTO>();
        for (Role role : roles) {
            out.add(mapper.map(role, RoleDTO.class));
        }
        return new ResponseEntity<List<RoleDTO>>(out, HttpStatus.OK);
    }
 
 
   
    //-------------------Retrieve Single Role--------------------------------------------------------
     
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoleDTO> getRole(@PathVariable("id") long id) throws Exception {
    	LOG.info("Fetching Role with id " + id);
        Role role = roleService.findById(id);
        if (role == null) {
        	LOG.info("Role with id " + id + " not found");
            return new ResponseEntity<RoleDTO>(HttpStatus.NOT_FOUND);
        }
        RoleDTO roleDTO = mapper.map(role, RoleDTO.class);
        return new ResponseEntity<RoleDTO>(roleDTO, HttpStatus.OK);
    }
 
     
     
    //-------------------Create a Role--------------------------------------------------------
     
    @RequestMapping(value = "/add/", method = RequestMethod.POST, headers = {JSON_API_CONTENT_HEADER})
    public ResponseEntity<Void> createRole(@RequestBody RoleDTO role,    UriComponentsBuilder ucBuilder) throws Exception {
    	LOG.info("Creating Role " + role.getRolename());
 
        if (roleService.isRolePresent(role.getRolename())) {
        	LOG.info("A Role with name " + role.getRolename() + " already exist");
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }
 
        Role nRole = new Role();
        nRole.setRolename(role.getRolename());
        nRole.setRoledesc(role.getRoledesc());
        nRole.setRoletype(role.getRoletype());

        List<Permission> permissionList = new ArrayList<Permission>();
        if (role.getPermissions()!=null && !role.getPermissions().isEmpty()){
        	for (PermissionDTO permission: role.getPermissions()){
        		if (permission.getId()>0){
        			permissionList.add(mapper.map(permission, Permission.class));
        		}
        	}
        }
    	nRole.setPermissions(permissionList);
    	nRole.setOrganization(mapper.map(role.getOrganization(), Organization.class));
    	
        roleService.insert(nRole);

        
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/role/{id}").buildAndExpand(role.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }
 
    
     
    //------------------- Update a Role --------------------------------------------------------
     
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.PUT)
    public ResponseEntity<RoleDTO> updateRole(@PathVariable("id") long id, @RequestBody RoleDTO role) throws Exception {
    	LOG.info("Updating Role " + id);
         
        Role currentRole = roleService.findById(id);
         
        if (currentRole==null) {
        	LOG.info("Role with id " + id + " not found");
            return new ResponseEntity<RoleDTO>(HttpStatus.NOT_FOUND);
        }
 
        currentRole.setRolename(role.getRolename());
        currentRole.setRoledesc(role.getRoledesc());
        currentRole.setRoletype(role.getRoletype());
        
        List<Permission> permissionList = new ArrayList<Permission>();
        if (role.getPermissions()!=null && !role.getPermissions().isEmpty()){
        	for (PermissionDTO permission: role.getPermissions()){
        		if (permission.getId()>0){
        			permissionList.add(mapper.map(permission, Permission.class));
        		}
        	}
        }
        currentRole.setPermissions(permissionList);
        currentRole.setOrganization(mapper.map(role.getOrganization(), Organization.class));
         
        roleService.update(currentRole);
        return new ResponseEntity<RoleDTO>(role, HttpStatus.OK);
    }
 
    
    
    //------------------- Delete a Role --------------------------------------------------------
     
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<RoleDTO> deleteRole(@PathVariable("id") long id) throws Exception {
    	LOG.info("Fetching & Deleting Role with id " + id);
 
        Role role = roleService.findById(id);
        if (role == null) {
        	LOG.info("Unable to delete. Role with id " + id + " not found");
            return new ResponseEntity<RoleDTO>(HttpStatus.NOT_FOUND);
        }
 
        roleService.delete(role);
        return new ResponseEntity<RoleDTO>(HttpStatus.NO_CONTENT);
    }
 
     
    
    
}
