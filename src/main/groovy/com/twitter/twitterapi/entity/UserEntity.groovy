package com.twitter.twitterapi.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.core.GrantedAuthority

import java.time.LocalDate

@Document(collection = "users")
class UserEntity {

    @Id
    String id

    @Indexed(unique = true)
    String username

    String password

    @Indexed(unique = true)
    String email

    String phone

    String address

    String fullName

    String gender

    LocalDate dateOfBirth

    List<GrantedAuthority> authorities = []

    @DBRef(lazy = true)
    Set<PostEntity> posts = new HashSet<>()

    void addPost(PostEntity post) {
        posts.add(post)
        post.setUser(this)
    }

    void removePost(PostEntity post) {
        posts.remove(post)
        post.setUser(null)
    }

}