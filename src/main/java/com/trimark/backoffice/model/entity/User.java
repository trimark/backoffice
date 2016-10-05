package com.trimark.backoffice.model.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.trimark.backoffice.framework.data.JPAEntity;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(indexes = {  @Index(name="email_idx", columnList = "email", unique = true),
                    @Index(name="displayName_idx", columnList = "display_name") })
@Access(AccessType.FIELD)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class User extends JPAEntity<Long> implements UserDetails {
/*
    public enum Role {
        USER,
        ADMIN
    }
*/
    private String email;
    private @JsonIgnore String password;
    private boolean enabled;
    private String displayName;

    private @JsonIgnore Integer loginCount;
    private Date currentLoginAt;
    private Date lastLoginAt;
    private @JsonIgnore String currentLoginIp;
    private @JsonIgnore String lastLoginIp;

    private static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @OneToOne(fetch = FetchType.EAGER)  
    @JoinTable(name = "user_roles",  
        joinColumns        = {@JoinColumn(name = "user_id", referencedColumnName = "id")},  
        inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")}  
    )
    private Role role;

    @OneToOne(fetch = FetchType.EAGER)  
    @JoinTable(name = "user_organizations",  
        joinColumns        = {@JoinColumn(name = "user_id", referencedColumnName = "id")},  
        inverseJoinColumns = {@JoinColumn(name = "organization_id", referencedColumnName = "id")}  
    )
    private Organization organization;

    @Column @Email @NotNull @NotBlank
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonIgnore @Column(nullable = false, length = 60)
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @JsonIgnore @Column(nullable = false)
    public boolean isEnabled() {
        return this.enabled;
    }

    public boolean getEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Column(nullable = false)
    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Column(nullable = false)
    public Organization getOrganization() {
        return this.organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    @Column(name="display_name")
    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @JsonIgnore @Column
    public Integer getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    @Column
    public Date getCurrentLoginAt() {
        return currentLoginAt;
    }

    public void setCurrentLoginAt(Date currentLoginAt) {
        this.currentLoginAt = currentLoginAt;
    }

    @Column
    public Date getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(Date lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    @JsonIgnore @Column
    public String getCurrentLoginIp() {
        return currentLoginIp;
    }

    public void setCurrentLoginIp(String currentLoginIp) {
        this.currentLoginIp = currentLoginIp;
    }

    @JsonIgnore @Column
    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    /**
     * Method to create the hash of the password before storing
     *
     * @param pass
     *
     * @return SHA hash digest of the password
     */
    public static synchronized String hashPassword(String pass) {
        return passwordEncoder.encode(pass);
    }

    public static synchronized boolean doesPasswordMatch(String rawPass, String encodedPass) {
        return passwordEncoder.matches(rawPass, encodedPass);
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", enabled=" + enabled +
                ", role=" + role +
                ", displayName='" + displayName + '\'' +
                ", loginCount=" + loginCount +
                ", currentLoginAt=" + currentLoginAt +
                ", lastLoginAt=" + lastLoginAt +
                ", currentLoginIp='" + currentLoginIp + '\'' +
                ", lastLoginIp='" + lastLoginIp + '\'' +
                '}';
    }

    @JsonIgnore @Transient
    public Set<Permission> getPermissions() {
        Set<Permission> perms = new HashSet<Permission>();
        if (role!=null){
        	perms.addAll(role.getPermissions());
        }
        return perms;
    }

    @JsonIgnore @Override @Transient
    public Collection<GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        authorities.add(getRole());
        authorities.addAll(getPermissions());
        return authorities;
    }

	@Override
	public String getUsername() {
		return this.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return false;
	}
}
