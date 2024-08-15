package com.meenachinmay.chatapp

import com.meenachinmay.chatapp.controller.ChatController
import com.meenachinmay.chatapp.model.Message
import com.meenachinmay.chatapp.model.User
import com.meenachinmay.chatapp.service.ChatService
import com.pusher.rest.Pusher
import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBodyList

@SpringBootTest
class ChatControllerTests {

    private lateinit var chatController: ChatController
    private lateinit var chatService: ChatService
    private lateinit var webTestClient: WebTestClient

    @BeforeEach
    fun setup() {
        val pusher = mockk<Pusher>(relaxed = true)
        chatService = ChatService(pusher)
        chatController = ChatController(chatService)
        webTestClient = WebTestClient.bindToController(chatController).build()
    }

    @Test
    fun `join chat should add user and return a user`() {
        val user = User("TestUser")

        webTestClient.post().uri("/api/chat/join")
            .bodyValue(user)
            .exchange()
            .expectStatus().isOk
            .expectBody(User::class.java)
            .isEqualTo(user)

        assert(chatService.users.containsKey("TestUser"))
    }

    @Test
    fun `leave chat should remove the user`() {
        val user = User("TestUser")
        chatService.addUser(user)

        webTestClient.post().uri("/api/chat/leave")
            .bodyValue(user)
            .exchange()
            .expectStatus().isOk

        assert(!chatService.users.containsKey("TestUser"))
    }

    @Test
    fun `getUsers should return list of users`() {
        chatService.addUser(User("User1"))
        chatService.addUser(User("User2"))

        webTestClient.get()
            .uri("/api/chat/users")
            .exchange()
            .expectStatus().isOk
            .expectBodyList<User>()
            .hasSize(2)
            .contains(User("User1"))
            .contains(User("User2"))
    }

    @Test
    fun `sendMessage should call chatService`() {
        val message = Message("Hello", "User2", "User1")
        val pusher = mockk<Pusher>(relaxed = true)
        val spyService = spyk(chatService)

        chatController = ChatController(spyService)
        webTestClient = WebTestClient.bindToController(chatController).build()

        webTestClient.post()
            .uri("/api/chat/messages")
            .bodyValue(message)
            .exchange()
            .expectStatus().isOk

        verify { spyService.sendMessage(message) }
    }
}