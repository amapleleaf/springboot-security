package com.maple.learn.jwtlogin.configure;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JwtTokenUtil {
    private static Logger log = LoggerFactory.getLogger(JwtTokenUtil.class);

    public static final String AUTH_HEADER_KEY = "Authorization";

    public static final String TOKEN_PREFIX = "Bearer ";

    private static String base64Secret="MDk4ZjZiY2Q0NjIxZDM3M2NhZGU0ZTgzMjYyN2I0ZjY=";
    private static long expiresSecond=7200000;
    /**
     * 解析jwt
     * @param jsonWebToken
     * @return
     */
    public static Claims parseJWT(String jsonWebToken) {
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(base64Secret))
                .parseClaimsJws(jsonWebToken).getBody();
        return claims;
    }

    /**
     * 构建jwt
     * @param userName
     * @param roleList
     * @return
     */
    public static String createJWT(String userName, List<String> roleList) {
        // 使用HS256加密算法
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //生成签名密钥
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(base64Secret);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //添加构成JWT的参数
        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JWT")
                // 可以将基本不重要的对象信息放到claims
                .claim("role", roleList)
                .claim("userName",userName)
                //.setSubject(username)           // 代表这个JWT的主体，即它的所有人
               // .setIssuer(jwtConfig.getIssuer())              // 代表这个JWT的签发主体；
                //.setIssuedAt(new Date())        // 是一个时间戳，代表这个JWT的签发时间；
                //.setAudience(audience.getName())          // 代表这个JWT的接收对象；
                .signWith(signatureAlgorithm, signingKey);
        //添加Token过期时间
        long TTLMillis = expiresSecond;
        if (TTLMillis >= 0) {
            long expMillis = nowMillis + TTLMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp)  // 是一个时间戳，代表这个JWT的过期时间；
                    .setNotBefore(now); // 是一个时间戳，代表这个JWT生效的开始时间，意味着在这个时间之前验证JWT是会失败的
        }
        //生成JWT
        return builder.compact();
    }

    /**
     * 从token中获取用户ID
     * @param token
     * @param base64Security
     * @return
     */
    public static String getUserId(String token, String base64Security){
        return parseJWT(token).get("userId", String.class);
    }

    public static void main(String[] args) {
       String jwtToken = JwtTokenUtil.createJWT("zhangsanf", new ArrayList<String>(){{add("admin");add("role");}});
       //jwtToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiMCIsInVzZXJJZCI6IjEyMzIiLCJ1c2VyTmFtZSI6InpoYW5nc2FuIiwiZXhwIjoxNTkxNzY2NDM5LCJuYmYiOjE1OTE3NTkyMzl9.JsDATxrcw1hN_HTWaoiBUhwY8YwAZhtmJfIZTLjrH8s";
       System.out.println("token:"+jwtToken);
        Claims parseJWT = JwtTokenUtil.parseJWT(jwtToken);
        System.out.println("Claims:"+parseJWT);
    }
}
