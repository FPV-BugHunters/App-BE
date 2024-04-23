package com.umb.tradingapp.security.dto;

import java.util.Set;

public class UserRolesDto {

    private final String userName;
    private final Set<String> roles;

    public UserRolesDto(String userName, Set<String> roles) {
        this.userName = userName;
        this.roles = roles;
    }

    public String getUserName() {
        return userName;
    }

    public Set<String> getRoles() {
        return roles;
    }

}
