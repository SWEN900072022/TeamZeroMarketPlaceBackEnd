package Util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import static Util.SQLUtil.getEnvValue;

public final class JWTUtil {
    public static String generateToken(String subject, Map<String, String> claimMap) {
        String secret = getEnvValue("JWT_SECRET");

        Date issueDate = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(issueDate);

        c.add(Calendar.HOUR, 1);
        Date expiredAt = c.getTime();

        JwtBuilder jwtBuilder = Jwts.builder()
                .setIssuer("TeamZero")
                .setSubject(subject)
                .setIssuedAt(issueDate)
                .setExpiration(expiredAt)
                .signWith(
                        Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8))
                );

        for(Map.Entry<String, String> entry : claimMap.entrySet()) {
            jwtBuilder.claim(entry.getKey(), entry.getValue());
        }
        return jwtBuilder.compact();
    }

    public static boolean validateToken(String jwt) throws UnsupportedEncodingException {
        String secret = getEnvValue("JWT_SECRET");
        Jws<Claims> jws;

        try{
            jws = Jwts.parserBuilder()
                    .setSigningKey(
                            Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8))
                    )
                    .build()
                    .parseClaimsJws(jwt);
        } catch (JwtException ex) {
            // Something has gone wrong
            return false;
        }
        return true;
    }

    public static String getSubject(String jwt) {
        String secret = getEnvValue("JWT_SECRET");
        String sub;

        try{
            sub = Jwts.parserBuilder()
                    .setSigningKey(
                            Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8))
                    )
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody()
                    .getSubject();
        } catch (JwtException ex) {
            return "";
            // Something has gone wrong
        }

        return sub;
    }

    public static String getClaim(String claim, String jwt) {
        String secret = getEnvValue("JWT_SECRET");
        String sub;

        try {
            sub = Jwts.parserBuilder()
                    .setSigningKey(
                            Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8))
                    )
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody()
                    .get(claim, String.class);
        } catch (JwtException e) {
            return "";
        }
        return sub;
    }
}
