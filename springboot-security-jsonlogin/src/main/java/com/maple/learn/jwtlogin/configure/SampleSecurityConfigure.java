package com.maple.learn.jwtlogin.configure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SampleSecurityConfigure extends WebSecurityConfigurerAdapter {
    @Autowired
    private  MyAuthenticationProvider myAuthenticationProvider;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                /*.formLogin().loginProcessingUrl("/login")
                .and()*/
                .csrf().disable()
                //.addFilter(new JWTLoginFilter(authenticationManager()))
                .addFilterAt(new JwtAuthenticationProcessingFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);
    }
    @Override
    public void configure(WebSecurity web) throws Exception {
        //ignore
        web.ignoring().antMatchers("/", "/css/**");
    }
   /* @Bean
    @Lazy
    public JwtAuthenticationProcessingFilter getJwtAuthenticationProcessingFilter() throws Exception {
        JwtAuthenticationProcessingFilter filter = new JwtAuthenticationProcessingFilter();
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }*/
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(myAuthenticationProvider);
    }


}
