package com.twitter.twitterapi.exception

class UserNotFoundException extends RuntimeException {
    UserNotFoundException() {
    }

    UserNotFoundException(String message) {
        super(message)
    }

    UserNotFoundException(String message, Throwable cause) {
        super(message, cause)
    }
}
