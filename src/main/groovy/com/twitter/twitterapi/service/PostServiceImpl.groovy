package com.twitter.twitterapi.service

import com.twitter.twitterapi.dto.PostDto
import com.twitter.twitterapi.entity.PostEntity
import com.twitter.twitterapi.entity.UserEntity
import com.twitter.twitterapi.exception.PostNotFoundException
import com.twitter.twitterapi.exception.UserNotFoundException
import com.twitter.twitterapi.mapper.PostMapper
import com.twitter.twitterapi.repository.PostRepository
import com.twitter.twitterapi.repository.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

import java.util.stream.Collectors

@Service
class PostServiceImpl implements PostService {

    private static final Logger log = LoggerFactory.getLogger(PostServiceImpl)

    private final PostRepository postRepository
    private final UserRepository userRepository
    private final PostMapper postMapper

    PostServiceImpl(PostRepository postRepository, UserRepository userRepository, PostMapper postMapper) {
        this.postRepository = postRepository
        this.userRepository = userRepository
        this.postMapper = postMapper
    }

    @Override
    PostDto create(PostDto postDto, String username) {
        UserEntity user = userRepository.findByUsername(username).orElseThrow({
            log.error("UserEntity with username {} not found", username)
            new UserNotFoundException("UserEntity with username " + username + " not found")
        })

        PostEntity post = postMapper.toEntity(postDto, user)

        PostEntity savedPost = postRepository.save(post)

        user.addPost(savedPost)
        userRepository.save(user)

        return postMapper.toDto(savedPost)
    }

    @Override
    PostDto update(PostDto postDto, String username) {
        PostEntity postToUpdate = postRepository.findById(postDto.id).orElseThrow({
            log.error("PostEntity with id {} not found", postDto.id)
            throw new PostNotFoundException("PostEntity with id " + postDto.id + " not found")
        })

        UserEntity user = userRepository.findByUsername(username).orElseThrow({
            log.error("UserEntity with username {} not found", username)
            throw new UserNotFoundException("UserEntity with username " + username + " not found")
        })

        if (!postToUpdate.user.id.equals(user.id)) {
            log.error("PostEntity with id {} does not belong to user {}", postDto.id, username)
            throw new PostNotFoundException("PostEntity with id " + postDto.id + " does not belong to user " + username)
        }

        postToUpdate.content = postDto.content

        PostEntity updatedPost = postRepository.save(postToUpdate)

        return postMapper.toDto(updatedPost)
    }

    @Override
    void delete(String postId, String username) {
        PostEntity postToDelete = postRepository.findById(postId).orElseThrow({
            log.error("PostEntity with id {} not found", postId)
            new PostNotFoundException("PostEntity with id " + postId + " not found")
        })

        UserEntity user = userRepository.findByUsername(username).orElseThrow({
            log.error("UserEntity with username {} not found", username)
            new UserNotFoundException("UserEntity with username " + username + " not found")
        })

        if (!postToDelete.user.id.equals(user.id)) {
            log.error("PostEntity with id {} does not belong to user {}", postId, username)
            throw new PostNotFoundException("PostEntity with id " + postId + " does not belong to user " + username)
        }

        user.setPosts(user.getPosts().stream()
                .filter { post -> !post.id.equals(postId) }
                .collect(Collectors.toSet()))
        userRepository.save(user)

        postRepository.deleteById(postId)
    }

    @Override
    List<PostDto> loadAllByUsername(String username) {
        Set<PostEntity> posts = userRepository.findByUsername(username)
                .orElseThrow({
                    log.error("UserEntity with username {} not found", username)
                    new UserNotFoundException("UserEntity with username " + username + " not found")
                })
                .getPosts()
        return posts.stream()
                .map(postMapper::toDto)
                .collect(Collectors.toList());
    }
}
