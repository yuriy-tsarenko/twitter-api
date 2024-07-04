package com.twitter.twitterapi.repository

import com.twitter.twitterapi.entity.UserEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository extends MongoRepository<UserEntity, String> {
    
    Optional<UserEntity> findByUsername(String username)
}
