package com.example.demo

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.transaction.Transactional
import kotlinx.coroutines.runBlocking
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*

@SpringBootApplication
class DemoApplication


fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}


@Entity
data class MyEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    var seqNo: Int = 0
)

@Repository
interface MyEntityRepository : JpaRepository<MyEntity, Long>

@Service
class MyEntityService(private val repository: MyEntityRepository) : CommandLineRunner {

    fun getEntity(id: Long): MyEntity? =
        repository.findById(id).orElse(null)

    @Transactional
    fun updateSeqNo(id: Long, newSeqNo: Int): MyEntity {
        val entity = repository.findById(id).orElseThrow { RuntimeException("Entity not found") }
        entity.seqNo = newSeqNo
        return repository.save(entity)
    }

    fun incrementSeqNo(id: Long): MyEntity {
        val entity = repository.findById(id).orElseThrow { RuntimeException("Entity not found") }
        Thread.sleep(3000)
        entity.seqNo++
        return repository.save(entity)
    }

    /**
     * 複数のControllerでも呼び出し順が保証される。
     */
    @Synchronized
    fun test(id: Long): MyEntity? {
        val result = incrementSeqNo(id)
        println(result.seqNo)
        return getEntity(id)
    }

    fun test2(id: Long): MyEntity? {
        val result = incrementSeqNo(id)
        println(result.seqNo)
        return getEntity(id)
    }

    override fun run(vararg args: String?) {
        val myEntity = getEntity(1)
        if (myEntity == null) {
            repository.save(MyEntity())
        }
    }

}

@RestController
@RequestMapping("/entities2")
class MyEntityController2(private val service: MyEntityService): MyEntityController(service) {

}

@RestController
@RequestMapping("/entities")
class MyEntityController(private val service: MyEntityService) {

    @GetMapping("/{id}")
    fun getEntity(@PathVariable id: Long): MyEntity? =
        service.getEntity(id)

    @GetMapping("/{id}/seqNo")
    fun updateSeqNo(@PathVariable id: Long, @RequestParam seqNo: Int): MyEntity =
        service.updateSeqNo(id, seqNo)

    @GetMapping("/{id}/increment")
    fun incrementSeqNo(@PathVariable id: Long): MyEntity =
        service.incrementSeqNo(id)

    @GetMapping("/{id}/test")
    fun test(@PathVariable id: Long): MyEntity? {
        return runBlocking {
            println("Started")
            val result = service.test(id)
            println("finish")
            result
        }
    }

}
