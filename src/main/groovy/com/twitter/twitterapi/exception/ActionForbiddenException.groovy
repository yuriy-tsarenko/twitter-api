package com.twitter.twitterapi.exception

class ActionForbiddenException extends RuntimeException {

    ActionForbiddenException(String message, Throwable cause) {
        super(message, cause)
    }

    ActionForbiddenException(String message) {
        super(message)
    }
}
