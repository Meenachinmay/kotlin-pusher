package com.meenachinmay.chatapp

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

        // Mock Pusher trigger calls
        every {
            pusher.trigger(any<String>(), any(), any())
        } returns mockk()

        // Test adding user
        val addedUser = chatService.addUser(user)
        assertEquals(user, addedUser)
        assertTrue(chatService.getUsers().contains(user))
        verify { pusher.trigger("new-login", "user-connected", user) }

        // Test removing user
        chatService.removeUser(user)
        assertFalse(chatService.getUsers().contains(user))
        verify { pusher.trigger("new-login", "user-disconnected", user) }

        // Verify Pusher was called exactly twice (once for add, once for remove)
        verify(exactly = 2) {
            pusher.trigger(match<String> { it == "new-login" }, any(), any())
        }
    }

}
