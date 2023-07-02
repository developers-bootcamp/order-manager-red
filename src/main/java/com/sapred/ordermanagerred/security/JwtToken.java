package com.sapred.ordermanagerred.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sapred.ordermanagerred.model.RoleOptions;
import com.sapred.ordermanagerred.model.User;
import com.sapred.ordermanagerred.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtToken {

    @Autowired
    private RoleRepository roleRepository;
    private static final String SECRET = "secret";
    private static final long EXPIRATION_TIME = 864_000_000; // 10 days

    // פונקציה שמקבלת משתמש ומחזירה טוקן
    public String generateToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET.getBytes());
        String access_token = JWT.create()
                .withClaim("companyId", user.getCompanyId().getId())
                .withClaim("userId", user.getId())
                .withClaim("roleId", user.getRoleId().getId())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(algorithm);
        return access_token;
    }

    // פונקציה שמקבלת טוקן ומחזירה קוד של חברה
    public String getCompanyIdFromToken(String jwtToken) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        DecodedJWT decodedJWT = JWT.require(algorithm).build().verify(jwtToken);
        String companyId = decodedJWT.getClaim("companyId").asString();
        return companyId;
    }

    // פונקציה שמקבלת טוקן ומחזירה קוד של משתמש
    public String getUserIdFromToken(String jwtToken) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        DecodedJWT decodedJWT = JWT.require(algorithm).build().verify(jwtToken);
        String userId = decodedJWT.getClaim("userId").asString();
        return userId;
    }

    // פונקציה שמקבלת טוקן ומחזירה קוד של תפקיד
    public RoleOptions getRoleIdFromToken(String jwtToken) {
        Algorithm algorithm = Algorithm.HMAC256(SECRET);
        DecodedJWT decodedJWT = JWT.require(algorithm).build().verify(jwtToken);
        String roleId = decodedJWT.getClaim("roleId").asString();
        RoleOptions role = roleRepository.findById(roleId).get().getName();
        return role;
    }
}