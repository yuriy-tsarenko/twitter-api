package com.twitter.twitterapi

import com.twitter.twitterapi.config.MongoTestConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

@ActiveProfiles("test")
@SpringBootTest(classes = [MongoTestConfiguration])
class TwitterApiApplicationTest extends Specification {

    def "context loads"() {
        expect:
        true
    }

}
