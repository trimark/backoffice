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
import com.trimark.backoffice.model.dto.OrganizationDTO;
import com.trimark.backoffice.model.dto.PermissionDTO;
import com.trimark.backoffice.model.entity.Organization;
import com.trimark.backoffice.model.entity.Permission;
import com.trimark.backoffice.service.OrganizationService;

@RestController
@RequestMapping("organization")
public class OrganizationController extends BaseController {
    private static Logger LOG = LoggerFactory.getLogger(OrganizationController.class);
    private Mapper mapper = DozerBeanMapperSingletonWrapper.getInstance();

    @Autowired 
    private OrganizationService organizationService;


    //-------------------Retrieve All Organizations--------------------------------------------------------
    
    @RequestMapping(value = "/list/", method = RequestMethod.GET)
    public ResponseEntity<List<OrganizationDTO>> listAllOrganizations() throws Exception {
        List<Organization> organizations = (List<Organization>) organizationService.getOrganizations(1, 1000, Order.asc("organizationname"));
        if(organizations.isEmpty()){
            return new ResponseEntity<List<OrganizationDTO>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        List<OrganizationDTO> out = new ArrayList<OrganizationDTO>();
        for (Organization organization : organizations) {
        	System.out.println("organization.getParent()"+organization.getParent());
        	System.out.println("organization.getChildren()"+organization.getChildren());
            out.add(mapper.map(organization, OrganizationDTO.class));
        }
        return new ResponseEntity<List<OrganizationDTO>>(out, HttpStatus.OK);
    }
 
 
   
    //-------------------Retrieve Single Organization--------------------------------------------------------
     
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<OrganizationDTO> getOrganization(@PathVariable("id") long id) throws Exception {
    	LOG.info("Fetching Organization with id " + id);
        Organization organization = organizationService.findById(id);
        if (organization == null) {
            return new ResponseEntity<OrganizationDTO>(HttpStatus.NOT_FOUND);
        }
        OrganizationDTO organizationDTO = mapper.map(organization, OrganizationDTO.class);
        return new ResponseEntity<OrganizationDTO>(organizationDTO, HttpStatus.OK);
    }
 
     
     
    //-------------------Create a Organization--------------------------------------------------------
     
    @RequestMapping(value = "/add/", method = RequestMethod.POST, headers = {JSON_API_CONTENT_HEADER})
    public ResponseEntity<Void> createOrganization(@RequestBody OrganizationDTO organization,    UriComponentsBuilder ucBuilder) throws Exception {
    	LOG.info("Creating Organization " + organization.getOrganizationname());
 
        if (organizationService.isOrganizationPresent(organization.getOrganizationname())) {
        	LOG.info("A Organization with name " + organization.getOrganizationname() + " already exist");
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }
 
        Organization nOrganization = new Organization();
        nOrganization.setOrganizationname(organization.getOrganizationname());
        nOrganization.setOrganizationdesc(organization.getOrganizationdesc());
        OrganizationDTO parent = organization.getParent();
        if (parent!=null && parent.getId()>0){
        	nOrganization.setParent(mapper.map(parent, Organization.class));
        } else {
        	nOrganization.setParent(null);
        }

        List<Organization> children = new ArrayList<Organization>();
        if (organization.getChildren()!=null && !organization.getChildren().isEmpty()){
        	for (OrganizationDTO child: organization.getChildren()){
        		if (child.getId()>0){
        			children.add(mapper.map(child, Organization.class));
        		}
        	}
        }
        nOrganization.setChildren(children);
    	
        organizationService.insert(nOrganization);
 
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/organization/{id}").buildAndExpand(organization.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }
 
    
     
    //------------------- Update a Organization --------------------------------------------------------
     
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.PUT)
    public ResponseEntity<OrganizationDTO> updateOrganization(@PathVariable("id") long id, @RequestBody OrganizationDTO organization) throws Exception {
    	LOG.info("Updating Organization " + id);
         
        Organization currentOrganization = organizationService.findById(id);
         
        if (currentOrganization==null) {
        	LOG.info("Organization with id " + id + " not found");
            return new ResponseEntity<OrganizationDTO>(HttpStatus.NOT_FOUND);
        }
 
        currentOrganization.setOrganizationname(organization.getOrganizationname());
        currentOrganization.setOrganizationdesc(organization.getOrganizationdesc());
        OrganizationDTO parent = organization.getParent();
        if (parent!=null && parent.getId()>0){
        	currentOrganization.setParent(mapper.map(parent, Organization.class));
        } else {
        	currentOrganization.setParent(null);
        }

        List<Organization> children = new ArrayList<Organization>();
        if (organization.getChildren()!=null && !organization.getChildren().isEmpty()){
        	for (OrganizationDTO child: organization.getChildren()){
        		if (child.getId()>0){
        			children.add(mapper.map(child, Organization.class));
        		}
        	}
        }
        currentOrganization.setChildren(children);
         
        organizationService.update(currentOrganization);
        return new ResponseEntity<OrganizationDTO>(organization, HttpStatus.OK);
    }
 
    
    
    //------------------- Delete a Organization --------------------------------------------------------
     
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<OrganizationDTO> deleteOrganization(@PathVariable("id") long id) throws Exception {
    	LOG.info("Fetching & Deleting Organization with id " + id);
 
        Organization organization = organizationService.findById(id);
        if (organization == null) {
        	LOG.info("Unable to delete. Organization with id " + id + " not found");
            return new ResponseEntity<OrganizationDTO>(HttpStatus.NOT_FOUND);
        }
 
        organizationService.delete(organization);
        return new ResponseEntity<OrganizationDTO>(HttpStatus.NO_CONTENT);
    }
 
     
    
    
}
