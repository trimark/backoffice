package com.trimark.backoffice.model.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class OrganizationDTO {

	private long id;
    private String organizationname;
    private String organizationdesc;
	private OrganizationDTO parent;
	private List<OrganizationDTO> children;
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrganizationname() {
        return organizationname;
    }

    public void setOrganizationname(String organizationname) {
        this.organizationname = organizationname;
    }

    public String getOrganizationdesc() {
        return organizationdesc;
    }

    public void setOrganizationdesc(String organizationdesc) {
        this.organizationdesc = organizationdesc;
    }

	public OrganizationDTO getParent() {
		return parent;
	}

	public void setParent(OrganizationDTO parent) {
		this.parent = parent;
	}

	public List<OrganizationDTO> getChildren() {
		return children;
	}

	public void setChildren(List<OrganizationDTO> children) {
		this.children = children;
	}

	@Override
    public String toString() {
        return String.format("%s(id=%d, organizationname='%s')", 
                this.getClass().getSimpleName(), 
                this.getId(), this.getOrganizationname());
    }
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof OrganizationDTO))
			return false;
		OrganizationDTO other = (OrganizationDTO) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}