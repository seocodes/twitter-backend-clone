package com.seocodes.spring_security.repository;

import com.seocodes.spring_security.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

// Repository: camada de acesso a dados, responsável pela comunicação com o banco de dados para realizar operações como o CRUD
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
}
