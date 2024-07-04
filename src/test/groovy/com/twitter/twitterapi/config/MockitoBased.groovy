package com.twitter.twitterapi.config

import org.mockito.MockitoAnnotations
import spock.lang.Specification

class MockitoBased extends Specification{
    def setup() {
        MockitoAnnotations.initMocks(this)
    }
}
