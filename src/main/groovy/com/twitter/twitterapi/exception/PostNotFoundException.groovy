package com.twitter.twitterapi.exception;

class PostNotFoundException extends RuntimeException {
    PostNotFoundException(String message) {
        super(message)
    }
}