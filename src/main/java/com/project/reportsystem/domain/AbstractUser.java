package com.project.reportsystem.domain;

import com.project.reportsystem.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Collection;
import java.util.Collections;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractUser implements UserDetails {
    private Long id;

    @NotEmpty(message = "Please provide email")
    @Email(message = "Email does not match pattern: example@gmail.com")
    private String email;

    @NotEmpty(message = "Please provide password")
    @Pattern(regexp = "([^\\s][A-Za-z\\d]{5,15})", message = "Password should be 6 to 15 characters long")
    private String password;

    @NotEmpty(message = "Please provide name")
    @Pattern(regexp = "([A-Z])([a-z]{1,40})|([А-ЯІЇЄ]([a-яіїє]{1,30}))", message = "Name should be at least 2 characters long and should not contain numbers")
    private String name;

    @NotEmpty(message = "Please provide surname")
    @Pattern(regexp = "([A-Z])([a-z]{1,40})|([А-ЯІЇЄ]([a-яіїє]{1,30}))", message = "Surname should be at least 2 characters long and should not contain numbers")
    private String surname;

    @NotEmpty(message = "Please provide patronymic")
    @Pattern(regexp = "([A-Z])([a-z]{1,40})|([А-ЯІЇЄ]([a-яіїє]{1,30}))", message = "Patronymic should be at least 2 characters long and should not contain numbers")
    private String patronymic;

    @NotNull(message = "Please provide type")
    private Role role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(getRole());
    }

    @Override
    public String getUsername() {
        return getEmail();
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
