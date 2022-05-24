package com.tender.utils;

import com.tender.constants.TenderConstants;
import com.tender.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {

    //generate token for user
    public static String generateToken(User userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("iss", TenderConstants.JWT_ISSUER);
        claims.put("name", userDetails.getName());
        claims.put("role", userDetails.getRole());
        String token = Jwts.builder().setClaims(claims).setSubject(userDetails.getUserId()).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TenderConstants.JWT_TOKEN_VALIDITY * 1000)).setNotBefore(new Date())
                .signWith(SignatureAlgorithm.HS512, TenderConstants.JWT_TOKEN_KEY).compact();
        return token;
    }
}
