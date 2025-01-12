package sample.redis.repository

interface RedisLockRepository {
    fun lock(key: String, value: String, leaseTime: Long): Boolean
    fun unlock(key: String)
}