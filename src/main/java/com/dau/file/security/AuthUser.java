package com.dau.file.security;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class AuthUser extends User {
    private final com.dau.file.entity.User user;

    public AuthUser(String username, String password, Collection<? extends GrantedAuthority> authorities, com.dau.file.entity.User user) {
        super(username, password, authorities);
        this.user = user;
    }

}
