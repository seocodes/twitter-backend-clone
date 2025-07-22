package com.seocodes.spring_security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
// Coloca "interceptadores"/filtros (autenticação, autorização e outras configurações) antes das requisições chegarem nos controllers
public class SecurityConfig {
    @Bean  // Bean = objeto controlado pelo Spring IoC (um objeto comum do Java, mas as configs, ciclo de vida e dependências são manipuladas pelo Spring)
    private SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // Todas as requisições precisam ser autenticadas para serem processadas
        http.authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
                .csrf(csrf -> csrf.disable())  // Só é legal pra facilitar testes em ambiente local, não fazer em produção
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults())) // Indica que vai usar o JWT - com configurações padrões
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // Sessão STATELESS pois estamos usando o JWT (que é STATELESS)
//Stateless: o servidor não precisa manter o estado da sessão do usuário. Em vez disso, o token contém todas as informações necessárias para autenticação e autorização, sendo armazenado no cliente (geralmente no navegador).

        return http.build();
    }

}
