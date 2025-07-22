package com.seocodes.spring_security.controller;

import com.seocodes.spring_security.controller.dto.LoginRequest;
import com.seocodes.spring_security.controller.dto.LoginResponse;
import com.seocodes.spring_security.entities.Role;
import com.seocodes.spring_security.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.stream.Collectors;

@RestController
public class TokenController {
    private final JwtEncoder jwtEncoder;  // Para fazer a criptografia do token
    private final UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;

    public TokenController(JwtEncoder jwtEncoder,
                           UserRepository userRepository,
                           BCryptPasswordEncoder passwordEncoder) {
        this.jwtEncoder = jwtEncoder;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest){
        var user = userRepository.findByUsername(loginRequest.username());

        if(user.isEmpty() || !user.get().isLoginCorrect(loginRequest, passwordEncoder)){
            throw new BadCredentialsException("user or password is invalid.");
        }

        var now = Instant.now();
        var expiresIn = 300L; // 300 segundos -> 5 minutos
        var scopes = user.get().getRoles()
                .stream().map(Role::getName)
                .collect(Collectors.joining(" "));

        // Configuração dos atributos JSON (claims)
        var claims = JwtClaimsSet.builder()
                .issuer("seocodes-backend")
                .subject(user.get().getUserId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiresIn))
                .claim("scope",scopes)
                .build();

        // Para realmente ter o token JWT com aqueles claims ali de cima
        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return ResponseEntity.ok(new LoginResponse(jwtValue, expiresIn));
    }
}
