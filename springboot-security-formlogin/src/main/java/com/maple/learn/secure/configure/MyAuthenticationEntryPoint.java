package com.maple.learn.secure.configure;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 1、详见ExceptionTranslationFilter中，
 * AuthenticationEntryPoint 用来解决匿名用户访问无权限资源时的异常
 * AccessDeineHandler 用来解决认证过的用户访问无权限资源时的异常
 *
 * 2、默认的LoginUrlAuthenticationEntryPoint
 * http..loginPage("/userlogin")->AbstractAuthenticationFilterConfigurer.setLoginPage（）
 * 方法会创建默认的LoginUrlAuthenticationEntryPoint
 */
public class MyAuthenticationEntryPoint  implements AuthenticationEntryPoint {
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        redirectStrategy.sendRedirect(request, response, "/login");
    }
}
