package com.example

import com.example.repository.PostRepository
import com.example.service.PostService
import kotlinx.coroutines.runBlocking
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.util.StopWatch

@SpringBootApplication
class Application(val postService: PostService) : CommandLineRunner {
    override fun run(vararg args: String?) {
        val stopWatch = StopWatch()
        stopWatch.start()
        val out = runBlocking { postService.list() }
        stopWatch.stop()
        println("Hello for ${out.size} "+stopWatch.totalTimeSeconds)
    }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args) {
    }
}
