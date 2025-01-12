package sample.redis.repository

import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.time.Duration

@Repository
class RedisLockRepositoryImpl(
    private val redisTemplate: RedisTemplate<String, String>
): RedisLockRepository {

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun lock(key: String, value: String, leaseTime: Long): Boolean {
        log.info("START LOCK | KEY : {}", key)

        return redisTemplate.opsForValue()
            .setIfAbsent(key, value, Duration.ofMillis(leaseTime)) == true
    }

    override fun unlock(key: String) {
        redisTemplate.delete(key)
        log.info("END LOCK | KEY : {}", key)
    }
}