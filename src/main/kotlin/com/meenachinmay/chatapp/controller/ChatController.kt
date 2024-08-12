package com.meenachinmay.chatapp.controller

import com.meenachinmay.chatapp.model.User
import com.meenachinmay.chatapp.service.ChatService
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/chat")
class ChatController(private val chatService: ChatService) {
    private val logger = LoggerFactory.getLogger(ChatController::class.java)

    @PostMapping("/join")
    fun joinChat(@RequestBody user: User): User {
        logger.info("User joining chat: $user")
        return chatService.addUser(user)
    }

    @PostMapping("/leave")
    fun leaveChat(@RequestBody user: User) {
        logger.info("User leaving chat: $user")
        chatService.removeUser(user)
    }

    @GetMapping("/users")
    fun getUsers(): List<User> {
        logger.info("Fetching all users")
        return chatService.getUsers()
    }
}