package com.example.demo

import jakarta.persistence.*
import jakarta.transaction.Transactional
import kotlinx.coroutines.runBlocking
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock
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
class NumberingService(private val repository: MyEntityRepository){
    @Transactional(Transactional.TxType.REQUIRES_NEW)
//    @Transactional
    fun updateSeqNo(id: Long, newSeqNo: Int): MyEntity {
        val entity = repository.findById(id).orElseThrow { RuntimeException("Entity not found") }
        entity.seqNo = newSeqNo
        return repository.save(entity)
    }
}

@Service
class MyEntityService(private val repository: MyEntityRepository, val numberingService: NumberingService) : CommandLineRunner {

    fun getEntity(id: Long): MyEntity? =
        repository.findById(id).orElse(null)

    @Synchronized
    fun incrementSeqNo(id: Long): MyEntity {
        val entity = repository.findById(id).orElseThrow { RuntimeException("Entity not found") }
        entity.seqNo++
        return numberingService.updateSeqNo(id, entity.seqNo)
    }

    @Transactional
    fun test(id: Long): MyEntity? {
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
class MyEntityController2(private val service: MyEntityService) : MyEntityController(service) {

}

@RestController
@RequestMapping("/entities")
class MyEntityController(private val service: MyEntityService) {

    @GetMapping("/{id}/test")
    fun test(@PathVariable id: Long): MyEntity? {
//        return synchronized(this) {
//            val result = service.test(id)
//            result
//        }
        val result = service.test(id)
        return result
    }

}
