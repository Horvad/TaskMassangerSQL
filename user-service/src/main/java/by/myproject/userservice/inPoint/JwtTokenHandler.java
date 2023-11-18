package by.myproject.userservice.inPoint;

import by.myproject.userservice.config.properties.JWTProperty;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.xml.crypto.Data;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class JwtTokenHandler {
    private final JWTProperty property;

    public JwtTokenHandler(JWTProperty property) {
        this.property = property;
    }

    public String generateAccessToken(Map<String,Object> mapClaims){
        return Jwts.builder()
                .setSubject(mapClaims.get("mail").toString())
                .setClaims(mapClaims)
                .setIssuer(property.getIssuer())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+ TimeUnit.DAYS.toMillis(7)))
                .signWith(SignatureAlgorithm.ES512,property.getSecret())
                .compact();
    }

    public String getUserName(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(property.getSecret())
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public Date getExpirationDate(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(property.getSecret())
                .parseClaimsJws(token)
                .getBody();
        return claims.getExpiration();
    }


}
