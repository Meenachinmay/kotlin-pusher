package com.meenachinmay.chatapp

import com.meenachinmay.chatapp.controller.ChatController
import com.meenachinmay.chatapp.model.Message
import com.meenachinmay.chatapp.model.User
import com.meenachinmay.chatapp.service.ChatService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
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
        chatService = mockk()
        chatController = ChatController(chatService)
        webTestClient = WebTestClient.bindToController(chatController).build()
    }

    @Test
    fun `join chat should add user and return a user` () {
        val user = User("TestUser")
        every { chatService.addUser(any()) } returns user

        webTestClient.post().uri("/api/chat/join")
            .bodyValue(user)
            .exchange()
            .expectStatus().isOk
            .expectBody(User::class.java)
            .isEqualTo(user)

        verify { chatService.addUser(user) }
    }

    @Test
    fun `leave chat should remove the user`  () {
        val user = User("TestUser")
        every { chatService.removeUser(any()) } returns Unit

        webTestClient.post().uri("/api/chat/leave")
            .bodyValue(user)
            .exchange()
            .expectStatus().isOk

        verify { chatService.removeUser(user) }

    }

    @Test
    fun `getUsers should return list of users`() {
        val users = listOf(User("User1"), User("User2"))
        every { chatService.getUsers() } returns users

        webTestClient.get()
            .uri("/api/chat/users")
            .exchange()
            .expectStatus().isOk
            .expectBodyList<User>()
            .hasSize(2)
            .contains(User("User1"))
            .contains(User("User2"))

        verify { chatService.getUsers() }
    }

    @Test
    fun `sendMessage should call chatService`() {
        val message = Message("Hello", "User2", "User1")
        every { chatService.sendMessage(any()) } returns Unit

        webTestClient.post()
            .uri("/api/chat/messages")
            .bodyValue(message)
            .exchange()
            .expectStatus().isOk

        verify { chatService.sendMessage(message) }
    }

}
