package com.organization.gsoc.Config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class GoogleOAuth2Principal implements OAuth2User {

    private final OAuth2User delegate;
    private final String email;

    public GoogleOAuth2Principal(
            OAuth2User delegate,
            String email
    ) {
        this.delegate = delegate;
        this.email = email;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return delegate.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return delegate.getAuthorities();
    }

    @Override
    public String getName() {
        return email;
    }
}