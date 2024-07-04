package com.twitter.twitterapi.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document
class PostEntity {
    @Id
    String id
    String content
    @DBRef
    UserEntity user
}
