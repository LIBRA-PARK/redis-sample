package sample.redis.service

import org.springframework.stereotype.Service
import sample.redis.repository.RedisLockRepository

@Service
class SampleService(
    private val redisLockRepository: RedisLockRepository
) {

    fun sample(id: String, value: String) {
        if(!redisLockRepository.lock(id, value, 3000L)) {
            throw RuntimeException()
        }

        redisLockRepository.unlock(id)
    }
}