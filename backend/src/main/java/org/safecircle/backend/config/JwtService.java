package org.safecircle.backend.config;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.safecircle.backend.enums.UserType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

@Component
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(long id, UserType role) throws JOSEException {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, 1);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(String.valueOf(id))
                .claim("role", role)
                .claim("type", "access")
                .expirationTime(cal.getTime())
                .build();

        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        SignedJWT signedJWT = new SignedJWT(header, claimsSet);
        JWSSigner signer = new MACSigner(secret);

        signedJWT.sign(signer);

        return signedJWT.serialize();
    }

    public String generateRefreshToken(long id, UserType role) throws JOSEException {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 7);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(String.valueOf(id))
                .claim("role", role)
                .claim("type", "refresh")
                .expirationTime(cal.getTime())
                .build();

        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);
        SignedJWT signedJWT = new SignedJWT(header, claimsSet);
        JWSSigner signer = new MACSigner(secret);

        signedJWT.sign(signer);

        return signedJWT.serialize();
    }

    public boolean validateToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(secret);
            return signedJWT.verify(verifier) && !isTokenExpired(signedJWT);
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean isTokenExpired(SignedJWT signedJWT) throws java.text.ParseException {
        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        return expirationTime.before(new Date());
    }

    public static String getSubject(String token) throws java.text.ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        return signedJWT.getJWTClaimsSet().getSubject();
    }

    public static UserType getRoleFromToken(String token) throws java.text.ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        String role = (String) signedJWT.getJWTClaimsSet().getClaim("role");
        return UserType.valueOf(role);
    }
    public static String getClaim(String token, String claim) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        return signedJWT.getJWTClaimsSet().getStringClaim(claim);
    }

}