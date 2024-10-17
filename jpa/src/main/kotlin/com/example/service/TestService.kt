package com.example.service

import com.example.entity.Post
import com.example.repository.PostRepository
import kotlinx.coroutines.*
import org.springframework.stereotype.Service

@Service
class PostService(private val postRepository: PostRepository) {

    suspend fun list(): List<Post> =  withContext(Dispatchers.IO){
        val result1 = async (Dispatchers.IO){
            delay(3000)
            postRepository.findAll()
        }

        val result2 = async (Dispatchers.IO) {
            delay(3000)
            postRepository.findAll()
        }
        val res1 = result1.await()
        println("First result ${res1.size}")
        val res2 = result2.await()
        println("Second result ${res2.size}")
        res2
    }


}