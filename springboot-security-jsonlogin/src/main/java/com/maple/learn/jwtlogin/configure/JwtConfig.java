package com.maple.learn.jwtlogin.configure;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
public class JwtConfig {
    @Value("${jwt.subject}")
    private String subject;
    @Value("${jwt.issuer}")
    private String issuer;
    @Value("${jwt.base64.secret}")
    private String base64Secret;
    @Value("${jwt.expires.second}")
    private long expiresSecond;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getBase64Secret() {
        return base64Secret;
    }

    public void setBase64Secret(String base64Secret) {
        this.base64Secret = base64Secret;
    }

    public long getExpiresSecond() {
        return expiresSecond;
    }

    public void setExpiresSecond(long expiresSecond) {
        this.expiresSecond = expiresSecond;
    }
}
