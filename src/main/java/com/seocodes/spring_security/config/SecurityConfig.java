package com.seocodes.spring_security.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration  // Declara uma classe que define Beans
@EnableWebSecurity // Coloca "interceptadores"/filtros (autenticação, autorização e outras configurações) antes das requisições chegarem nos controllers
@EnableMethodSecurity // Para alguns métodos do UserController - Autorização/@PreAuthorize
public class SecurityConfig {
    @Value("${jwt.public.key}")  // Aponta para o application.properties
    private RSAPublicKey publicKey;

    @Value("${jwt.private.key}")  // Aponta para o application.properties
    private RSAPrivateKey privateKey;

    @Bean  // Bean = objeto controlado pelo Spring IoC (um objeto comum do Java, mas as configs, ciclo de vida e dependências são manipuladas pelo Spring)
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // Todas as requisições precisam ser autenticadas para serem processadas
        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST,"/login").permitAll() // Definir que a rota /login não precisa de autenticação
                        .requestMatchers(HttpMethod.POST,"/users").permitAll()
                        .anyRequest().authenticated())
                .csrf(csrf -> csrf.disable())  // Só é legal pra facilitar testes em ambiente local, não fazer em produção
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults())) // Indica que vai usar o JWT - com configurações padrões
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // Sessão STATELESS pois estamos usando o JWT (que é STATELESS)
//Stateless: o servidor não precisa manter o estado da sessão do usuário. Em vez disso, o token contém todas as informações necessárias para autenticação e autorização, sendo armazenado no cliente (geralmente no navegador).

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder(){
        // Criando o decoder a partir da chave pública
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

    @Bean
    public JwtEncoder jwtEncoder(){
        // JWK é como se fosse a chave do JWT, pra depois fazer o encoding. É bem complexozinho
        JWK jwk = new RSAKey.Builder(this.publicKey).privateKey(privateKey).build();
        var jwks = new ImmutableJWKSet<>(new JWKSet(jwk));

        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
