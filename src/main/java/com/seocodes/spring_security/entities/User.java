package com.seocodes.spring_security.entities;

import com.seocodes.spring_security.controller.dto.LoginRequest;
import jakarta.persistence.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "tb_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Gerar automaticamente o UUID
    @Column(name = "user_id")
    private UUID userId;

    @Column(unique = true) // Para esse campo ser único no banco de dados
    private String username;

    private String password;


    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER) // Vários usuários podem ter várias roles e várias roles são associadas a vários usuários. REQUER TABELA INTERMEDIÁRIA
    // O cascade serve para qualquer operação que fizermos na tabela de users será replicado aqui (em cascata). O fetch define como vamos puxar do banco (EAGER = Carrega dados relacionados imediatamente, bom porque sempre precisaremos saber a role para autenticação!)
    @JoinTable( // Tabela intermediária devido à relação ManyToMany
            name = "tb_users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles; // Set para não ter dados repetidos

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    // Metodo para comparar senhas
    public boolean isLoginCorrect(LoginRequest loginRequest, PasswordEncoder passwordEncoder){
        // Compara a senha "raw" (do LoginRequest) com a senha criptografada do banco
        return passwordEncoder.matches(loginRequest.password(),this.password);
    }
}
