package model;

import java.util.Set;

public class SessionUser {
    private Long id;
    private Set<Role> roles;

    public SessionUser(Long id, Set<Role> roles) {
        this.id = id;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
