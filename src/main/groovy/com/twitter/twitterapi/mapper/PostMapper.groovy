package com.twitter.twitterapi.mapper

import com.twitter.twitterapi.dto.PostDto
import com.twitter.twitterapi.entity.PostEntity
import com.twitter.twitterapi.entity.UserEntity
import org.springframework.stereotype.Component

@Component
class PostMapper {

    PostEntity toEntity(PostDto postDto, UserEntity user) {
        return Optional.ofNullable(postDto)
                .map {
                    new PostEntity(
                            id: it.id,
                            content: it.content,
                            user: user
                    )
                }
                .orElse(null)
    }

    PostDto toDto(PostEntity postEntity) {
        return Optional.ofNullable(postEntity)
                .map {
                    new PostDto(
                            id: it.id,
                            content: it.content,
                            username: it.user?.username
                    )
                }
                .orElse(null)
    }
}