package com.tender.utils;

import com.sun.istack.NotNull;
import com.tender.constants.TenderConstants;
import com.tender.entity.User;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

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

    //Verifies Id a JWT is Valid Or not
    public static boolean validateJwtToken(String authToken) {
        try {
            Claims claims = Jwts.parser().setSigningKey(TenderConstants.JWT_TOKEN_KEY).parseClaimsJws(authToken).getBody();
            String issuer = claims.getIssuer();
            if(!TenderConstants.JWT_ISSUER.equalsIgnoreCase(issuer)) {
                return true;
            }
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    //Verifies Id a JWT is Valid Or not
    public static boolean validateJwtTokenWithRole(String authToken, @NotNull String role) {
        try {
            Claims claims = Jwts.parser().setSigningKey(TenderConstants.JWT_TOKEN_KEY).parseClaimsJws(authToken).getBody();
            String issuer = claims.getIssuer();
            String jwtRole = claims.get("role", String.class);
            if(!TenderConstants.JWT_ISSUER.equalsIgnoreCase(issuer)) {
                return false;
            }

            if(!role.equalsIgnoreCase(jwtRole)) {
                return false;
            }
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
