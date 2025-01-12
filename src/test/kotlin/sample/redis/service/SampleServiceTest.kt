package sample.redis.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.web.servlet.function.ServerResponse.async
import java.util.concurrent.CountDownLatch
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

@SpringBootTest
class SampleServiceTest @Autowired constructor(
    private val redisService: SampleService
) {
    private val numberOfThreads = 5

    @Test
    fun `동일한 key에 대해 lock이 한정적으로 동작하는지 확인`(){
        val readyLatch = CountDownLatch(numberOfThreads)
        val startLatch = CountDownLatch(1)
        val doneLatch = CountDownLatch(numberOfThreads)

        // 성공 횟수와 실패 횟수를 카운트할 Atomic 변수
        val successCount = AtomicInteger(0)
        val failureCount = AtomicInteger(0)

        // ExecutorService를 통해 스레드 풀 생성
        val executor: ExecutorService = Executors.newFixedThreadPool(numberOfThreads)
        val testKey = "test-key"
        val testValue = "test-value"

        repeat(numberOfThreads) {
            executor.submit {
                try {
                    // 스레드 준비 완료 알림
                    readyLatch.countDown()
                    // 모든 스레드 동시 시작 대기
                    startLatch.await()

                    // sample 메서드 호출
                    redisService.sample(testKey, testValue)
                    // 정상적으로 락을 획득했으면 성공 카운트 증가
                    successCount.incrementAndGet()
                } catch (e: Exception) {
                    // 락 획득 실패 등 예외가 발생하면 실패 카운트 증가
                    failureCount.incrementAndGet()
                    println("락 획득 실패")
                } finally {
                    doneLatch.countDown()
                }
            }
        }

        // 모든 스레드 준비 대기
        readyLatch.await()
        // 모든 스레드 동시에 시작
        startLatch.countDown()
        // 모든 스레드 작업 완료 대기
        doneLatch.await()
        executor.shutdown()

    }
}