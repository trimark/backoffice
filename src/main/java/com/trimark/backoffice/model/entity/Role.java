package com.trimark.backoffice.model.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
@Table(name="roles")
@Access(AccessType.FIELD)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Role extends JPAEntity<Long> implements GrantedAuthority {

    @NotNull(message = "{error.roles.role.null}")
    @NotEmpty(message = "{error.roles.role.empty}")
    @Size(max = 50, message = "{error.roles.role.max}")
    @Column(name = "rolename", length = 50, unique=true)
    private String rolename;

    @NotNull 
    @NotBlank
    @Column(name = "roledesc", length = 255)
    private String roledesc;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_permissions",
        joinColumns        = {@JoinColumn(name = "role_id",       referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "permission_id", referencedColumnName = "id")}
    )
    private List<Permission> permissions = new ArrayList<Permission>(0);

    @OneToOne(fetch = FetchType.EAGER)  
    @JoinTable(name = "role_organizations",  
        joinColumns        = {@JoinColumn(name = "role_id", referencedColumnName = "id")},  
        inverseJoinColumns = {@JoinColumn(name = "organization_id", referencedColumnName = "id")}  
    )
    private Organization organization;

    @Column(name = "roletype", length = 12)
    private String roletype;
    
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

    public List<Permission> getPermissions() { 
        return permissions; 
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    @Column(nullable = false)
    public Organization getOrganization() {
        return this.organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    @Override
    public String toString() {
        return String.format("%s(id=%d, rolename='%s')", 
                this.getClass().getSimpleName(), 
                this.getId(), this.getRolename());
    }

	@Override
	public String getAuthority() {
		return getRolename();
	}


}
