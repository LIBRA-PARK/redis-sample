package sample.redis.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "spring.data.redis")
data class RedisProperties(
    var host: String = "localhost",
    var port: Int = 6379
)