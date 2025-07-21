package com.seocodes.spring_security.repository;

import com.seocodes.spring_security.entities.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TweetRepository extends JpaRepository<Tweet,Long> {
}
