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
    @Autowired
    private  MyAuthenctiationSuccessHandler myAuthenctiationSuccessHandler;
    @Autowired
    private MyAuthenctiationFailureHandler myAuthenctiationFailureHandler;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //http.sessionManagement().invalidSessionUrl();
        //允许所有用户访问"/"和"/home"
        http.authorizeRequests()
                .antMatchers("/login","/timeout").permitAll()
                //.antMatchers("/user/**").hasRole("USER")
                //其他地址的访问均需验证权限
                .anyRequest().authenticated()
                .and()
                .sessionManagement().invalidSessionUrl("/timeout")
                .and()
                .formLogin()
                //指定登录页是"/login"
                .loginPage("/login")

                .successHandler(myAuthenctiationSuccessHandler)
                //.defaultSuccessUrl("/login?error=true")//会创建默认的successHandler
                .failureHandler(myAuthenctiationFailureHandler)
                //.failureUrl("/home")//会创建默认的failureHandler
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
