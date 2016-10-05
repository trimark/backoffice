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
import com.trimark.backoffice.model.entity.Permission;
import com.trimark.backoffice.service.PermissionService;

@RestController
@RequestMapping("permission")
public class PermissionController extends BaseController {
    private static Logger LOG = LoggerFactory.getLogger(PermissionController.class);
    private Mapper mapper = DozerBeanMapperSingletonWrapper.getInstance();

    @Autowired 
    private PermissionService permissionService;


    //-------------------Retrieve All Permissions--------------------------------------------------------
    
    @RequestMapping(value = "/list/", method = RequestMethod.GET)
    public ResponseEntity<List<PermissionDTO>> listAllPermissions() throws Exception {
        List<Permission> permissions = (List<Permission>) permissionService.getPermissions(1, 1000, Order.asc("permissionname"));
        if(permissions.isEmpty()){
            return new ResponseEntity<List<PermissionDTO>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        List<PermissionDTO> out = new ArrayList<PermissionDTO>();
        for (Permission permission : permissions) {
            out.add(mapper.map(permission, PermissionDTO.class));
        }
        return new ResponseEntity<List<PermissionDTO>>(out, HttpStatus.OK);
    }
 
 
   
    //-------------------Retrieve Single Permission--------------------------------------------------------
     
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PermissionDTO> getPermission(@PathVariable("id") long id) throws Exception {
    	LOG.info("Fetching Permission with id " + id);
        Permission permission = permissionService.findById(id);
        if (permission == null) {
            return new ResponseEntity<PermissionDTO>(HttpStatus.NOT_FOUND);
        }
        PermissionDTO permissionDTO = mapper.map(permission, PermissionDTO.class);
        return new ResponseEntity<PermissionDTO>(permissionDTO, HttpStatus.OK);
    }
 
     
     
    //-------------------Create a Permission--------------------------------------------------------
     
    @RequestMapping(value = "/add/", method = RequestMethod.POST, headers = {JSON_API_CONTENT_HEADER})
    public ResponseEntity<Void> createPermission(@RequestBody PermissionDTO permission,    UriComponentsBuilder ucBuilder) throws Exception {
    	LOG.info("Creating Permission " + permission.getPermissionname());
 
        if (permissionService.isPermissionPresent(permission.getPermissionname())) {
        	LOG.info("A Permission with name " + permission.getPermissionname() + " already exist");
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }
 
        Permission nPermission = new Permission();
        nPermission.setPermissionname(permission.getPermissionname());
        nPermission.setPermissiondesc(permission.getPermissiondesc());
        nPermission.setGroup(permission.getGroup());
        nPermission.setCategory(permission.getCategory());
        nPermission.setRights(permission.getRights());
        permissionService.insert(nPermission);
 
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/permission/{id}").buildAndExpand(permission.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }
 
    
     
    //------------------- Update a Permission --------------------------------------------------------
     
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.PUT)
    public ResponseEntity<PermissionDTO> updatePermission(@PathVariable("id") long id, @RequestBody PermissionDTO permission) throws Exception {
    	LOG.info("Updating Permission " + id);
         
        Permission currentPermission = permissionService.findById(id);
         
        if (currentPermission==null) {
        	LOG.info("Permission with id " + id + " not found");
            return new ResponseEntity<PermissionDTO>(HttpStatus.NOT_FOUND);
        }
 
        currentPermission.setPermissionname(permission.getPermissionname());
        currentPermission.setPermissiondesc(permission.getPermissiondesc());
        currentPermission.setGroup(permission.getGroup());
        currentPermission.setCategory(permission.getCategory());
        currentPermission.setRights(permission.getRights());
         
        permissionService.update(currentPermission);
        return new ResponseEntity<PermissionDTO>(permission, HttpStatus.OK);
    }
 
    
    
    //------------------- Delete a Permission --------------------------------------------------------
     
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<PermissionDTO> deletePermission(@PathVariable("id") long id) throws Exception {
    	LOG.info("Fetching & Deleting Permission with id " + id);
 
        Permission permission = permissionService.findById(id);
        if (permission == null) {
        	LOG.info("Unable to delete. Permission with id " + id + " not found");
            return new ResponseEntity<PermissionDTO>(HttpStatus.NOT_FOUND);
        }
 
        permissionService.delete(permission);
        return new ResponseEntity<PermissionDTO>(HttpStatus.NO_CONTENT);
    }
 
     
    
    
}
