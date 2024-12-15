package org.example

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    // List of URLs to make parallel requests
    val urls = List(50){"http://localhost:8080/entities/1/test"}

    // Create a list of deferred results (asynchronous tasks)
    val requests = urls.map { url ->
        async {
            val data = fetchData(url)
            println("Response from $url: $data")
        }
    }

    // Wait for all requests to finish
    requests.awaitAll()

    println("All requests completed.")
}

suspend fun fetchData(url: String): String {
    val client = HttpClient(CIO)  // Using CIO engine for async requests
    return try {
        client.get(url).toString()
    } catch (e: Exception) {
        "Error fetching data from $url: ${e.message}"
    } finally {
        client.close() // Close the client after use
    }
}