package com.maple.learn.jwtlogin.configure;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maple.learn.jwtlogin.form.LoginForm;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JwtAuthenticationProcessingFilter extends AbstractAuthenticationProcessingFilter {
    public JwtAuthenticationProcessingFilter(AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher("/userlogin", "POST"));
        super.setAuthenticationManager(authenticationManager);
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
            ObjectMapper mapper = new ObjectMapper();
            UsernamePasswordAuthenticationToken authRequest = null;
            try (InputStream is = request.getInputStream()) {
                LoginForm loginForm = mapper.readValue(is, LoginForm.class);
                authRequest = new UsernamePasswordAuthenticationToken(
                        loginForm.getUserName(), loginForm.getPassword());
            } catch (IOException e) {
                e.printStackTrace();
                authRequest = new UsernamePasswordAuthenticationToken(
                        "", "");
            }
            return super.getAuthenticationManager().authenticate(authRequest);
    }
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);
        List<String> roleList =authResult.getAuthorities().stream().map(s -> s.getAuthority()).collect(Collectors.toList());
        String jwtToken = JwtTokenUtil.createJWT(authResult.getName(),roleList);

        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        Map<String,Object> result = new HashMap<>();
        result.put("error_no","0");
        result.put("accessToken",jwtToken);
        PrintWriter out=null;
        try {
            out = response.getWriter();
            out.print(new ObjectMapper().writeValueAsString(result));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(out!=null) {
                out.close();
            }
        }
    }

    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
         SecurityContextHolder.clearContext();

        String error="";
        if (failed instanceof UsernameNotFoundException || failed instanceof BadCredentialsException) {
            error = "用户名或密码错误";
        } else if (failed instanceof DisabledException) {
            error = "账户已禁用";
        } else if (failed instanceof LockedException) {
            error = "账户已锁定";
        } else if (failed instanceof AccountExpiredException) {
            error = "账户已过期";
        } else if (failed instanceof CredentialsExpiredException) {
            error = "证书已过期";
        } else {
            error = "登录失败";
        }
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        Map<String,Object> result = new HashMap<>();
        result.put("error_no","-1");
        result.put("error_msg",error);
        PrintWriter out=null;
        try {
            out = response.getWriter();
            out.print(new ObjectMapper().writeValueAsString(result));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(out!=null) {
                out.close();
            }
        }
    }
}
