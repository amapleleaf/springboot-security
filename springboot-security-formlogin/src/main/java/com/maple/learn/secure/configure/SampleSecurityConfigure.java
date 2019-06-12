package com.maple.learn.secure.configure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SampleSecurityConfigure extends WebSecurityConfigurerAdapter {
    @Autowired
    private  MyUserDetailsService myUserDetailsService;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //允许所有用户访问"/"和"/home"
        http.authorizeRequests()
               // .antMatchers("/", "/css/**").permitAll()
                //其他地址的访问均需验证权限
                .anyRequest().authenticated()
                .and()
                .formLogin()
                //指定登录页是"/login"
                .loginPage("/login").failureUrl("/login?error=true")
                .defaultSuccessUrl("/home")//登录成功后默认跳转到"/home"
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/login")//退出登录后的默认url是"/login"
                .permitAll();
    }
    @Override
    public void configure(WebSecurity web) throws Exception {
        //ignore
        web.ignoring().antMatchers("/", "/css/**");
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder());
    }

}
