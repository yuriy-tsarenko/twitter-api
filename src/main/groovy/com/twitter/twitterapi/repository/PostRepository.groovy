package com.twitter.twitterapi.repository

import com.twitter.twitterapi.entity.PostEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface PostRepository extends MongoRepository<PostEntity, String> {
    Optional<PostEntity> findById(String id)
}