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
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import com.trimark.backoffice.framework.data.JPAEntity;

@Entity
@Table(name="organizations")
@Access(AccessType.FIELD)
public class Organization extends JPAEntity<Long> {

	@NotNull(message = "{error.organization.organizationname.null}")
    @NotEmpty(message = "{error.organization.organizationname.empty}")
    @Size(max = 50, message = "{error.organization.organizationname.max}")
    @Column(name = "organizationname", length = 50, unique=true)
    private String organizationname;
    
    @NotNull 
    @NotBlank
    @Column(name = "organizationdesc", length = 255)
    private String organizationdesc;
    
	@ManyToOne(optional = true, fetch = FetchType.EAGER)
	@JoinTable(name = "organizations_tree", 
	    joinColumns = { @JoinColumn(name = "child_id", referencedColumnName = "id") }, 
	    inverseJoinColumns = { @JoinColumn(name = "parent_id", referencedColumnName = "id") } )
	private Organization parent;
	
	@OneToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "organizations_tree", 
	    joinColumns = { @JoinColumn(name = "parent_id", referencedColumnName = "id") }, 
	    inverseJoinColumns = { @JoinColumn(name = "child_id", referencedColumnName = "id") } )
	private List<Organization> children = new ArrayList<Organization>(0);

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

    public Organization getParent() {
		return parent;
	}

	public void setParent(Organization parent) {
		this.parent = parent;
	}

	public List<Organization> getChildren() {
		return children;
	}

	public void setChildren(List<Organization> children) {
		this.children = children;
	}

	@Override
    public String toString() {
        return String.format("%s(id=%d, organizationname='%s')", 
                this.getClass().getSimpleName(), 
                this.getId(), this.getOrganizationname());
    }
}
