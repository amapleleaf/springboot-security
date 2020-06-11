package com.maple.learn.jwtlogin.configure;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MyUserDetailsService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(MyUserDetailsService.class);

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserDetails user=null;
        if(userName.equals("admin")){
            List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
            authList.add(new SimpleGrantedAuthority("USER"));
            authList.add(new SimpleGrantedAuthority("ADMIN"));
            String passpord = new BCryptPasswordEncoder().encode("admin");
            user = new User("admin", passpord, authList);
            logger.info("{}-->{}", userName, passpord);
        }else if(userName.equals("user")){
            List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
            authList.add(new SimpleGrantedAuthority("USER"));
            String passpord = new BCryptPasswordEncoder().encode("user");
            user = new User("user", passpord, authList);
            logger.info("{}-->{}", userName, passpord);
        }else{
            throw new UsernameNotFoundException("用户不存在！");
        }

        return user;
    }
}
