package com.maple.learn.secure.configure;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class MyFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {
    private Map<RequestMatcher, Collection<ConfigAttribute>> allRoleSource = new HashMap<>();
    public MyFilterInvocationSecurityMetadataSource(){
        Map<String,String> urlRoleMap = new HashMap<String,String>(){{
            put("/open/**","ROLE_ANONYMOUS");
            put("/home","ADMIN,USER");
            put("/admin/**","ADMIN");
            put("/user/**","ADMIN,USER");
        }};
        Map<RequestMatcher, Collection<ConfigAttribute>> loadRequestMap = new HashMap<>();
        for(Map.Entry<String,String> entry:urlRoleMap.entrySet()){
            loadRequestMap.put(new AntPathRequestMatcher(entry.getKey()),SecurityConfig.createList(entry.getValue().split(",")));
        }
        allRoleSource = loadRequestMap;
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        FilterInvocation fi = (FilterInvocation) object;
        HttpServletRequest request = fi.getRequest();
        //String url = fi.getRequestUrl();
        //String httpMethod = fi.getRequest().getMethod();
        for(Map.Entry<RequestMatcher, Collection<ConfigAttribute>> entry:allRoleSource.entrySet()){
            if(entry.getKey().matches(request)){
                return entry.getValue();
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

