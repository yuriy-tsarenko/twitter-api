package com.twitter.twitterapi

import com.twitter.twitterapi.config.MongoTestConfiguration
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@SpringBootTest(classes = [MongoTestConfiguration])
class TwitterApiApplicationTests {

    @Test
    void contextLoads() {
    }

}
