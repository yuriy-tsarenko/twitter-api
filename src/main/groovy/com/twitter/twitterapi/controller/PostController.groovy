package com.twitter.twitterapi.controller

import com.twitter.twitterapi.dto.PostDto
import com.twitter.twitterapi.service.PostService
import org.springframework.security.access.annotation.Secured
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/posts")
class PostController {

    private final PostService postService

    PostController(PostService postService) {
        this.postService = postService
    }

    @GetMapping
    @Secured("USER")
    List<PostDto> allUsersPosts(@RequestParam("username") String username) {
        return postService.loadAllByUsername(username)
    }

    @PostMapping
    @Secured("USER")
    PostDto create(@RequestBody PostDto postDto, @AuthenticationPrincipal String username) {
        return postService.create(postDto, username)
    }

    @PutMapping
    @Secured("USER")
    PostDto update(@RequestBody PostDto postDto, @AuthenticationPrincipal String username) {
        return postService.update(postDto, username)
    }

    @DeleteMapping("/{id}")
    @Secured("USER")
    void delete(@PathVariable String id, @AuthenticationPrincipal String username) {
        postService.delete(id, username)
    }
}