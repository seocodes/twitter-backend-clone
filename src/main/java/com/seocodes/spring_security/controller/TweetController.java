package com.seocodes.spring_security.controller;

import com.seocodes.spring_security.repository.TweetRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TweetController {
    private final TweetRepository tweetRepository;

    public TweetController(TweetRepository tweetRepository){
        this.tweetRepository = tweetRepository;

    }

    @PostMapping("/tweets")
    public ResponseEntity<Void> createTweet(){

    }
}
