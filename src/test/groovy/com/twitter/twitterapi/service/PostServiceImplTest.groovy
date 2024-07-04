package com.twitter.twitterapi.service

import com.twitter.twitterapi.config.MockitoBased
import com.twitter.twitterapi.dto.PostDto
import com.twitter.twitterapi.entity.PostEntity
import com.twitter.twitterapi.entity.UserEntity
import com.twitter.twitterapi.mapper.PostMapper
import com.twitter.twitterapi.repository.PostRepository
import com.twitter.twitterapi.repository.UserRepository
import org.mockito.InjectMocks
import org.mockito.Mock

import static org.junit.jupiter.api.Assertions.assertEquals
import static org.junit.jupiter.api.Assertions.assertNotNull
import static org.mockito.Mockito.when

class PostServiceImplTest extends MockitoBased {

    @Mock
    PostRepository postRepository

    @Mock
    UserRepository userRepository

    @Mock
    PostMapper postMapper

    @InjectMocks
    PostServiceImpl postService

    def "Create"() {
        given: "A PostDto and a username"
        def postDto = new PostDto(content: "Post content")
        def username = "testUser"
        def userEntity = new UserEntity(id: "1", username: username)
        def postEntity = new PostEntity(content: "Post content", user: userEntity)
        def savedPostEntity = new PostEntity(id: "1", content: "Post content", user: userEntity)
        def expectedPostDto = new PostDto(id: "1", content: "Post content", username: "1")

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity))
        when(postMapper.toEntity(postDto, userEntity)).thenReturn(postEntity)
        when(postRepository.save(postEntity)).thenReturn(savedPostEntity)
        when(postMapper.toDto(savedPostEntity)).thenReturn(expectedPostDto)

        when: "Create method is called"
        def result = postService.create(postDto, username)

        then: "The result should be the expected PostDto"
        assertNotNull(result)
        assertEquals(expectedPostDto.id, result.id)
        assertEquals(expectedPostDto.content, result.content)
        assertEquals(expectedPostDto.username, result.username)
    }

    def "Update"() {
        given: "A PostDto, postId and a username"
        def postDto = new PostDto(id: "1", content: "Updated content")
        def username = "testUser"
        def userEntity = new UserEntity(id: "1", username: username)
        def postEntity = new PostEntity(id: "1", content: "Old content", user: userEntity)
        def updatedPostEntity = new PostEntity(id: "1", content: "Updated content", user: userEntity)
        def expectedPostDto = new PostDto(id: "1", content: "Updated content", username: "1")

        when(postRepository.findById(postDto.id)).thenReturn(Optional.of(postEntity))
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity))
        when(postRepository.save(postEntity)).thenReturn(updatedPostEntity)
        when(postMapper.toDto(updatedPostEntity)).thenReturn(expectedPostDto)

        when: "Update method is called"
        def result = postService.update(postDto, username)

        then: "The result should be the expected PostDto"
        assertNotNull(result)
        assertEquals(expectedPostDto.id, result.id)
        assertEquals(expectedPostDto.content, result.content)
        assertEquals(expectedPostDto.username, result.username)
    }

    def "Delete"() {
        given: "A postId and a username"
        def postId = "1"
        def username = "testUser"
        def userEntity = new UserEntity(id: "1", username: username)
        def postEntity = new PostEntity(id: postId, user: userEntity)

        when(postRepository.findById(postId)).thenReturn(Optional.of(postEntity))
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(userEntity))

        when: "Delete method is called"
        postService.delete(postId, username)

        then: "The post should be deleted"
    }

    def "LoadAll"() {
        given: "A list of posts"
        def postEntity1 = new PostEntity(id: "1", content: "Post 1")
        def postEntity2 = new PostEntity(id: "2", content: "Post 2")
        def postEntities = [postEntity1, postEntity2]
        def postDto1 = new PostDto(id: "1", content: "Post 1", username: "1")
        def postDto2 = new PostDto(id: "2", content: "Post 2", username: "2")
        def postDtos = [postDto1, postDto2]

        when(postRepository.findAll()).thenReturn(postEntities)
        when(postMapper.toDto(postEntity1)).thenReturn(postDto1)
        when(postMapper.toDto(postEntity2)).thenReturn(postDto2)

        when: "LoadAll method is called"
        def result = postService.loadAll()

        then: "The result should be a list of PostDto"
        assertNotNull(result)
        assertEquals(postDtos.size(), result.size())
        assertEquals(postDtos, result)
    }
}
