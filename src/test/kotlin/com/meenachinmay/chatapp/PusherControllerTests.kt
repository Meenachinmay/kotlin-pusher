package com.meenachinmay.chatapp

import com.meenachinmay.chatapp.controller.PusherController
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.util.ReflectionTestUtils
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.reactive.function.BodyInserters

@SpringBootTest
@AutoConfigureWebTestClient
class PusherControllerTests {
//    @BeforeEach
//    fun setup() {
//        pusherController = PusherController()
//        ReflectionTestUtils.setField(pusherController, "appKey", "test-app-key")
//        ReflectionTestUtils.setField(pusherController, "appSecret", "test-app-secret")
//        webTestClient = WebTestClient.bindToController(pusherController).build()
//    }
    @Autowired
    private lateinit var webTestClient: WebTestClient

    @Test
    fun `authenticatePusherUser should return valid auth response` (): Unit = runBlocking {
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
    fun `authenticatePusherUser should return bad request when socket_id is missing` (): Unit = runBlocking {
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
    fun `authenticatePusherUser should return bad request when channel_name is missing` (): Unit = runBlocking {
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