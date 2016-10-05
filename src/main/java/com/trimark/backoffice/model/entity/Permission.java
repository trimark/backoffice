package com.trimark.backoffice.model.entity;


import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.trimark.backoffice.framework.data.JPAEntity;

@Entity
@Table(name="permissions")
@Access(AccessType.FIELD)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Permission extends JPAEntity<Long> implements GrantedAuthority {

	@NotNull(message = "{error.permission.permissionname.null}")
    @NotEmpty(message = "{error.permission.permissionname.empty}")
    @Size(max = 50, message = "{error.permission.permissionname.max}")
    @Column(name = "permissionname", length = 50, unique=true)
    private String permissionname;
    
    @NotNull 
    @NotBlank
    @Column(name = "permissiondesc", length = 255)
    private String permissiondesc;
    
    @NotNull 
    @NotBlank
    @Column(name = "grp", length = 50)
    private String group;

    @NotNull 
    @NotBlank
    @Column(name = "category", length = 50)
    private String category;
    
    @Column(name = "rights", length = 50)
    private String rights;
    
    
/*    
    @OneToMany(fetch = FetchType.EAGER)  
    @JoinTable(name = "role_permissions",   
        joinColumns        = {@JoinColumn(name = "permission_id", referencedColumnName = "id")},  
        inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}  
    )  
    private List<Role> permRoles;
*/
    
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

/*
    public List<Role> getPermRoles() {
        return permRoles;
    }

    public void setPermRoles(List<Role> permRoles) {
        this.permRoles = permRoles;
    }
*/
    @Override
    public String toString() {
        return String.format("%s(id=%d, permissionname='%s')", 
                this.getClass().getSimpleName(), 
                this.getId(), this.getPermissionname());
    }

	@Override
	public String getAuthority() {
		return permissionname;
	}

}
