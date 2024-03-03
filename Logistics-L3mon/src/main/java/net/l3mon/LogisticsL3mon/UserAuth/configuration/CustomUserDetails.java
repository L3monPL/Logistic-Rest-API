package net.l3mon.LogisticsL3mon.UserAuth.configuration;

import net.l3mon.LogisticsL3mon.UserAuth.entity.Role;
import net.l3mon.LogisticsL3mon.UserAuth.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private String username;
    private String password;
    private Integer companyId;
    private Role role;

    public CustomUserDetails(User user){
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.role = user.getRole();
        this.companyId = Math.toIntExact(user.getCompany().getId());
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
