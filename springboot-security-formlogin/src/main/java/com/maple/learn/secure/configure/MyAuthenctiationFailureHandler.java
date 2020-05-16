package com.maple.learn.secure.configure;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("myAuthenctiationFailureHandler")
public class MyAuthenctiationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    /*@Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String error="";
        if (exception instanceof UsernameNotFoundException || exception instanceof BadCredentialsException) {
            error = "用户名或密码错误";
        } else if (exception instanceof DisabledException) {
            error = "账户已禁用";
        } else if (exception instanceof LockedException) {
            error = "账户已锁定";
        } else if (exception instanceof AccountExpiredException) {
            error = "账户已过期";
        } else if (exception instanceof CredentialsExpiredException) {
            error = "证书已过期";
        } else {
            error = "登录失败";
        }
        request.getSession().setAttribute("error_info", error);
        //super.getRedirectStrategy().sendRedirect(request, response, "/login?error=true");
        super.setDefaultFailureUrl("/login?error=true");
        super.onAuthenticationFailure(request,response,exception);
    }*/
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
        String error="";
        if (exception instanceof UsernameNotFoundException || exception instanceof BadCredentialsException) {
            error = "用户名或密码错误";
        } else if (exception instanceof DisabledException) {
            error = "账户已禁用";
        } else if (exception instanceof LockedException) {
            error = "账户已锁定";
        } else if (exception instanceof AccountExpiredException) {
            error = "账户已过期";
        } else if (exception instanceof CredentialsExpiredException) {
            error = "证书已过期";
        } else {
            error = "登录失败";
        }
        request.getSession().setAttribute("error_info", error);
        super.setDefaultFailureUrl("/login?error=true");
       super.onAuthenticationFailure(request,response,exception);
       return;
    }

}
