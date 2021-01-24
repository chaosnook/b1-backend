package com.game.b1ingservice.payload.commons;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.game.b1ingservice.postgres.entity.WebUser;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
public class UserPrincipal implements UserDetails {

    private Long id;

    private String username;

    @JsonIgnore
    private String password;

    private String fullName;

    private Long agentId;

    private String agentName;

    private List<String> roles;

    private String role;

    private String prefix;

    public UserPrincipal() {
        super();
    }


    private Collection<? extends GrantedAuthority> authorities;

    public UserPrincipal(Long id, String username, String password, Long agentId, String role,
                         Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.agentId = agentId;
        this.role = role;
    }

    public static UserPrincipal create(WebUser user) {

        return new UserPrincipal(user.getId(), user.getUsername(), user.getPassword(), user.getAgent().getId(), "USER",
                null);
    }


    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
