package com.seocodes.spring_security.repository;

import com.seocodes.spring_security.entities.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repository: camada de acesso a dados, responsável pela comunicação com o banco de dados para realizar operações como o CRUD
@Repository
public interface TweetRepository extends JpaRepository<Tweet,Long> {
}
