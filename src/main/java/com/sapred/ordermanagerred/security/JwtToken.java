package com.sapred.ordermanagerred.security;

import com.auth0.jwt.JWT;
//import com.sapred.ordermanagerred.model.Users;
//import com.yael.leaning.model.User;
import com.auth0.jwt.algorithms.Algorithm;
import com.sapred.ordermanagerred.model.User;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtToken {

    private static final String SECRET = "secret";
    private static final long EXPIRATION_TIME = 864_000_000; // 10 days

    // פונקציה שמקבלת משתמש ומחזירה טוקן
    public String generateToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET.getBytes());
        String access_token = JWT.create()
                .withClaim("companyId", user.getCompanyId().getCurrency())
                .withClaim("userId", user.getId())
                .withClaim("roleId", user.getRoleId().getId())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(algorithm);
        return access_token;
    }
}