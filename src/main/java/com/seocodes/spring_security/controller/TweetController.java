package com.seocodes.spring_security.controller;

import com.seocodes.spring_security.controller.dto.CreateTweetDTO;
import com.seocodes.spring_security.entities.Role;
import com.seocodes.spring_security.entities.Tweet;
import com.seocodes.spring_security.repository.TweetRepository;
import com.seocodes.spring_security.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
public class TweetController {
    private final TweetRepository tweetRepository;
    private final UserRepository userRepository;

    public TweetController(TweetRepository tweetRepository, UserRepository userRepository){
        this.tweetRepository = tweetRepository;
        this.userRepository = userRepository;
    }

//NUMA BOA PRÁTICA, PARTE DISSO PODIA ESTAR NUMA SERVICE, MAS DEIXA ASSIM PARA SIMPLIFICAR
    @PostMapping("/tweets")
    public ResponseEntity<Void> createTweet(@RequestBody CreateTweetDTO dto,
                                            JwtAuthenticationToken token){  // O token é para identificar o usuário
        //.getName() pega o subject, se olhar no TokenController, o subject é o ID
        var user = userRepository.findById(UUID.fromString(token.getName()));

        var tweet = new Tweet();
        tweet.setUser(user.get());
        tweet.setContent(dto.content());

        tweetRepository.save(tweet);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/tweets/{id}")
    public ResponseEntity<Void> deleteTweet(@PathVariable("id") Long tweetId,
                                            JwtAuthenticationToken token){

        var user = userRepository.findById(UUID.fromString(token.getName()));
        var tweet = tweetRepository.findById(tweetId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        var isAdmin = user.get().getRoles()
                        .stream()
                        .anyMatch(role -> role.getName().equalsIgnoreCase(Role.Values.ADMIN.name()));

        // Verifica se o tweet pertence àquele usuário - lembrando que o .getName() pega o subject, que nesse caso é o ID
        // Também verifica se é um Admin (se for, pode excluir também)
        if (isAdmin || tweet.getUser().getUserId().equals(UUID.fromString(token.getName()))){
            tweetRepository.deleteById(tweetId);
        } else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok().build();
    }
}
