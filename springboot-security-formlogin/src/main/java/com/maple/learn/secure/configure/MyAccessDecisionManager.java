package com.maple.learn.secure.configure;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.vote.AbstractAccessDecisionManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.thymeleaf.util.StringUtils;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class MyAccessDecisionManager implements AccessDecisionManager {
    @Override
    public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException, InsufficientAuthenticationException {
        Iterator<ConfigAttribute> iterator = configAttributes.iterator();
        while (iterator.hasNext()) {

            if (authentication == null) {
                throw new AccessDeniedException("当前访问没有权限");
            }

            ConfigAttribute configAttribute = iterator.next();
            String needCode = configAttribute.getAttribute();

            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                if (StringUtils.equals(authority.getAuthority(), needCode)) {
                    return;
                }
            }
        }
        throw new AccessDeniedException("当前访问没有权限");
    }

    @Override
    public boolean supports(ConfigAttribute attribute) {
        return false;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
