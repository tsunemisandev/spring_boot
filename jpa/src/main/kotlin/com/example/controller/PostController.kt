package com.example.controller

import com.example.entity.Post
import com.example.service.PostService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/posts")
class PostController(private val postService: PostService) {

    @GetMapping
    suspend fun getAllPosts(): List<Post> = withContext(Dispatchers.IO) {
        postService.list()
    }

}