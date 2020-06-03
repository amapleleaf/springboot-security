package com.maple.learn.secure.configure;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class MyFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    // 这里的需要从DB加载
    private final Map<String,String> urlRoleMap = new HashMap<String,String>(){{
        put("/open/**","ROLE_ANONYMOUS");
        put("/home","ROLE_ADMIN");
        put("/admin/**","ROLE_ADMIN");
        put("/user/**","ROLE_ADMIN,ROLE_USER");
    }};


    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        FilterInvocation fi = (FilterInvocation) object;
        String url = fi.getRequestUrl();

        for(Map.Entry<String,String> entry:urlRoleMap.entrySet()){
            if(antPathMatcher.match(entry.getKey(),url)){
                return SecurityConfig.createList(entry.getValue());
            }
        }

        return null;

    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}

