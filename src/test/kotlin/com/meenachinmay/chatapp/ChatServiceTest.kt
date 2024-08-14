package com.meenachinmay.chatapp

import com.meenachinmay.chatapp.model.Message
import com.meenachinmay.chatapp.model.User
import com.meenachinmay.chatapp.service.ChatService
import com.pusher.rest.Pusher
import io.mockk.MockKStubScope
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ChatServiceTest {
    private lateinit var pusher: Pusher
    private lateinit var chatService: ChatService

    @BeforeEach
    fun setup() {
        pusher = mockk()
        chatService = ChatService(pusher)
    }

    @Test
    fun `add and remove user should update user list and trigger pusher events`() {
        val user = User("TestUser")

        every {
            pusher.trigger(any<String>(), any(), any())
        } returns mockk()

        val addedUser = chatService.addUser(user)
        assertEquals(user, addedUser)
        assertTrue(chatService.getUsers().contains(user))
        verify { pusher.trigger("new-login", "user-connected", user) }

        chatService.removeUser(user)
        assertFalse(chatService.getUsers().contains(user))
        verify { pusher.trigger("new-login", "user-disconnected", user) }

        verify(exactly = 2) {
            pusher.trigger(match<String> { it == "new-login" }, any(), any())
        }
    }

    @Test
    fun `getUsers should return set of all users`() {
        val user1 = User("User1")
        val user2 = User("User2")

        every {
            pusher.trigger(any<String>(), any(), any())
        } returns mockk()

        chatService.addUser(user1)
        chatService.addUser(user2)

        val result = chatService.getUsers()

        assertEquals(setOf(user1, user2), result.toSet())

        verify(exactly = 2) {
            pusher.trigger(match<String> { it == "new-login" }, "user-connected", any())
        }
    }

    @Test
    fun `sendMessage should trigger Pusher event with correct channel`() {
        val message = Message("Hello", "User2", "User1")

        // Mock the single-channel version of trigger
        every {
            pusher.trigger(any<String>(), any(), any())
        } returns mockk()

        chatService.sendMessage(message)

        // Verify the single-channel version of trigger
        verify {
            pusher.trigger(
                match<String> { it == "private-chat-User2" },
                "new-message",
                message
            )
        }
    }

}
