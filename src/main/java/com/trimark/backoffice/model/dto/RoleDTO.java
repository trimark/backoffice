package com.trimark.backoffice.model.dto;

import java.util.List;

public class RoleDTO {

	private long id;
    String rolename;
    String roledesc;
    String roletype;
    List <PermissionDTO> permissions;
    OrganizationDTO organization;
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<PermissionDTO> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<PermissionDTO> permissions) {
		this.permissions = permissions;
	}

	public String getRolename() {
        return rolename;
    }

    public void setRolename(String rolename) {
        this.rolename = rolename;
    }

	public String getRoledesc() {
        return roledesc;
    }

    public void setRoledesc(String roledesc) {
        this.roledesc = roledesc;
    }

	public String getRoletype() {
        return roletype;
    }

    public void setRoletype(String roletype) {
        this.roletype = roletype;
    }

    public OrganizationDTO getOrganization() {
        return organization;
    }

    public void setOrganization(OrganizationDTO organization) {
        this.organization = organization;
    }

    @Override
    public String toString() {
        return String.format("%s(id=%d, permissionname='%s')", 
                this.getClass().getSimpleName(), 
                this.getId(), this.getRolename());
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
		if (!(obj instanceof RoleDTO))
			return false;
		RoleDTO other = (RoleDTO) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}