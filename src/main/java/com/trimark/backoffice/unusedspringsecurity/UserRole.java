package com.trimark.backoffice.unusedspringsecurity;

import javax.persistence.*;

import com.trimark.backoffice.model.entity.User;

import java.io.Serializable;

//@Entity
//@Table(indexes = {  @Index(name="email_fk_idx", columnList = "email", unique = true) })
public class UserRole implements Serializable {
    private Integer userRoleId;
    private User user;
    private String role;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_role_id",
            unique = true, nullable = false)
    public Integer getUserRoleId() {
        return this.userRoleId;
    }

    public void setUserRoleId(Integer userRoleId) {
        this.userRoleId = userRoleId;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email", nullable = false)
    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Column(name = "role", nullable = false, length = 45)
    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
