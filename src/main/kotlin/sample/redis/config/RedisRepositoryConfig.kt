package sample.redis.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories

@Configuration
@EnableRedisRepositories
class RedisRepositoryConfig(
    private val properties: RedisProperties
) {
    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory =
        LettuceConnectionFactory(properties.host, properties.port)

    @Bean
    fun redisTemplate(): RedisTemplate<ByteArray, ByteArray> =
        RedisTemplate<ByteArray, ByteArray>().apply {
            setConnectionFactory(redisConnectionFactory())
        }
}