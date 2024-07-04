package com.twitter.twitterapi.service

import com.twitter.twitterapi.dto.PostDto

interface PostService {
    PostDto create(PostDto postDto, String username)

    PostDto update(PostDto postDto, String username)

    void delete(String postId, String username)

    List<PostDto> loadAll();

}