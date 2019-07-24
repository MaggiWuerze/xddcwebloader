package de.maggiwuerze.xdccloader.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;

@Entity(name = "userdetail")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    Long id;

    @Column(nullable = false)
    Boolean active = true;

    @Column(nullable = false, unique = true)
    String name;

    @Column(nullable = false, unique = true)
    String password;

    @Column(nullable = false)
    LocalDateTime creationDate = LocalDateTime.now();

    @Column(nullable = false)
    String userRole;

    @Column()
    Boolean locked = false;

    @Column()
    Boolean initialized = false;

    @Column()
    LocalDateTime expirationDate;

    @Column()
    LocalDateTime sessionValidUntil ;

    @Transient
    String ROLE_PREFIX = "ROLE_";

    public User(String name, String password, String userRole, boolean initialized) {
        this.name = name;
        this.password = password;
        this.userRole = userRole;
        this.initialized = initialized;
    }

    public User() {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> list = new ArrayList();

        list.add(new SimpleGrantedAuthority(ROLE_PREFIX + userRole));

        return list;

    }

    @Override
    public String getUsername() {
        return getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return getExpirationDate() != null? getExpirationDate().isBefore(LocalDateTime.now()): true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !getLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return getExpirationDate() != null? getExpirationDate().isBefore(LocalDateTime.now()): true;
    }

    @Override
    public boolean isEnabled() {
        return getActive();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public LocalDateTime getSessionValidUntil() {
        return sessionValidUntil;
    }

    public void setSessionValidUntil(LocalDateTime sessionValidUntil) {
        this.sessionValidUntil = sessionValidUntil;
    }

    public Boolean getInitialized() {
        return initialized;
    }

    public void setInitialized(Boolean initialized) {
        this.initialized = initialized;
    }
}
