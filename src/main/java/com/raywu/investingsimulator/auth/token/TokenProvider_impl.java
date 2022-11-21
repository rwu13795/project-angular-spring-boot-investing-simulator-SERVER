package com.raywu.investingsimulator.auth.token;

import com.raywu.investingsimulator.exception.exceptions.BadRequestException;
import com.raywu.investingsimulator.exception.exceptions.InvalidTokenException;
import com.raywu.investingsimulator.utility.EnvVariable;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class TokenProvider_impl implements TokenProvider {

    private final Key tokenSecret;
    private final Long tokenExpirationMS = (long) (1000 * 60 * 60);  // 1-hour
    private final Long refreshTokenExpirationMS = (long) (1000 * 60 * 60 * 24 * 7); // 7-days


    @Autowired
    public TokenProvider_impl(EnvVariable env) {
        this.tokenSecret = this.convertStringToKey(env.TOKEN_SECRET());
    }

    @Override
    public Token generateAccessToken(String email, int id) {
        Date now = new Date();
        long duration = tokenExpirationMS;
        Date expiryDate = new Date(now.getTime() + duration);
        // Since setSubject() only accepts String, I need to concat the email and id "{email}_{id}"
        // then split this string after parsing the JWT in the filter
        String emailAndId = email + "_" + id;

        String token = Jwts.builder()
                .setSubject(emailAndId)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(tokenSecret, SignatureAlgorithm.HS256)
                .compact();

        return new Token(Token.TokenType.ACCESS, token, duration,
                LocalDateTime.ofInstant(expiryDate.toInstant(), ZoneId.systemDefault()));
    }

    @Override
    public Token generateRefreshToken(String email, int id) {
        Date now = new Date();
        long duration = refreshTokenExpirationMS;
        Date expiryDate = new Date(now.getTime() + duration);
        String emailAndId = email + "_" + id;

        String token = Jwts.builder()
                .setSubject(emailAndId)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(tokenSecret, SignatureAlgorithm.HS256)
                .compact();
        return new Token(Token.TokenType.REFRESH, token, duration,
                LocalDateTime.ofInstant(expiryDate.toInstant(), ZoneId.systemDefault()));
    }

    @Override
    public String getUserInfoFromToken(String token) {
        // the "Jwts.parser()" is deprecated, need to use "parserBuilder()"
        Claims claims = Jwts.parserBuilder().setSigningKey(tokenSecret).build()
                .parseClaimsJws(token).getBody();

        return claims.getSubject();
    }

//    @Override
//    public LocalDateTime getExpiryDateFromToken(String token) {
//        // the "Jwts.parser()" is deprecated, need to use "parserBuilder()"
//        Claims claims = Jwts.parserBuilder().setSigningKey(tokenSecret).build()
//                .parseClaimsJws(token).getBody();
//        return LocalDateTime.ofInstant(claims.getExpiration().toInstant(), ZoneId.systemDefault());
//    }

    @Override
    public boolean validateToken(String token) {
        if(token == null) throw new InvalidTokenException("Invalid token");

        try {
            // the "Jwts.parser()" is deprecated, need to use "parserBuilder()"
            Jwts.parserBuilder().setSigningKey(tokenSecret).build().parse(token);
//            System.out.println("validating---------"+email);
            return true;
        } catch (ExpiredJwtException | MalformedJwtException
                 | SignatureException | IllegalArgumentException
                 | DecodingException exc) {
            System.out.println("invalid------------------");
            exc.printStackTrace();
        }
        return false;
    }

    private Key convertStringToKey(String tokenSecret) {
        byte[] keyBytes = Decoders.BASE64.decode(tokenSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

