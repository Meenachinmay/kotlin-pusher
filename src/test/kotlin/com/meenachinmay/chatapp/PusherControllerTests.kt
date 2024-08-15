package com.meenachinmay.chatapp

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.core.env.Environment
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.reactive.function.BodyInserters

@SpringBootTest(
    classes = [ChatappApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
class PusherControllerTests : ApplicationContextAware {

    private lateinit var applicationContext: ApplicationContext

    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Autowired
    private lateinit var env: Environment

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.applicationContext = applicationContext
    }

    @Test
    fun contextLoads() {
        println("contextLoads test started")
        assert(::applicationContext.isInitialized) { "ApplicationContext should be initialized" }
        assert(applicationContext.beanDefinitionNames.isNotEmpty()) { "ApplicationContext should have beans" }
        assert(env.getProperty("pusher.app-id") != null) { "Pusher app ID should be set" }
        println("contextLoads test completed")
    }

    @Test
    fun `authenticatePusherUser should return valid auth response`(): Unit = runBlocking {
        val formData = LinkedMultiValueMap<String, String>()
        formData.add("socket_id", "123.123")
        formData.add("channel_name", "private-chat-testuser")

        val response = webTestClient.post()
            .uri("/pusher/auth")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData(formData))
            .exchange()
            .expectStatus().isOk
            .expectBody(String::class.java)
            .returnResult()

        println("Response body: ${response.responseBody}")
        assert(response.responseBody?.contains("\"auth\":") == true)
    }

    @Test
    fun `authenticatePusherUser should return bad request when socket_id is missing`(): Unit = runBlocking {
        val formData = LinkedMultiValueMap<String, String>()
        formData.add("channel_name", "private-chat-testuser")

        webTestClient.post()
            .uri("/pusher/auth")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData(formData))
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .isEqualTo("Missing socket_id")
    }

    @Test
    fun `authenticatePusherUser should return bad request when channel_name is missing`(): Unit = runBlocking {
        val formData = LinkedMultiValueMap<String, String>()
        formData.add("socket_id", "123.123")

        webTestClient.post()
            .uri("/pusher/auth")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(BodyInserters.fromFormData(formData))
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .isEqualTo("Missing channel_name")
    }
}