package com.twitter.twitterapi.repository

import com.twitter.twitterapi.entity.UserEntity
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository extends MongoRepository<UserEntity, String> {
    
    Optional<UserEntity> findByUsername(String username)
}
