package com.laptops.ALaptop.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.laptops.ALaptop.security.Permission.DATA_READ;
import static com.laptops.ALaptop.security.Permission.DATA_WRITE;

public enum Role {

    GUEST(Sets.newHashSet(DATA_READ)),
    ADMIN(Sets.newHashSet(DATA_READ, DATA_WRITE));

    private final Set<Permission> permissionSet;

    Role(Set<Permission> permissionSet){
        this.permissionSet = permissionSet;
    }
    public Set<Permission> getPermissionSet(){
        return permissionSet;
    }
    public Set<SimpleGrantedAuthority> getSimpleGrantedAuthority(){
        return getPermissionSet().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission_info()))
                .collect(Collectors.toSet());
    }
}
