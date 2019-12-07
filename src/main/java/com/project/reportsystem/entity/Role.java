package com.project.reportsystem.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    INSPECTOR, ADMIN, LEGAL_TAXPAYER, INDIVIDUAL_TAXPAYER,
    ;

    @Override
    public String getAuthority() {
        return name();
    }
}
