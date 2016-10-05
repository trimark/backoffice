package com.trimark.backoffice.model.dto;

public class PermissionDTO {

	private long id;
    private String permissionname;
    private String permissiondesc;
    private String group;
    private String category;
    private String rights;
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPermissionname() {
        return permissionname;
    }

    public void setPermissionname(String permissionname) {
        this.permissionname = permissionname;
    }

    public String getPermissiondesc() {
        return permissiondesc;
    }

    public void setPermissiondesc(String permissiondesc) {
        this.permissiondesc = permissiondesc;
    }

    public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getRights() {
		return rights;
	}

	public void setRights(String rights) {
		this.rights = rights;
	}

	@Override
    public String toString() {
        return String.format("%s(id=%d, permissionname='%s')", 
                this.getClass().getSimpleName(), 
                this.getId(), this.getPermissionname());
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
		if (!(obj instanceof PermissionDTO))
			return false;
		PermissionDTO other = (PermissionDTO) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
}