package com.meenachinmay.chatapp.controller

import com.meenachinmay.chatapp.model.User
import com.meenachinmay.chatapp.model.Message
import com.meenachinmay.chatapp.service.ChatService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

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

    @PostMapping("/messages")
    fun sendMessage(@RequestBody message: Message) {
        logger.info("Sending message: $message")
        chatService.sendMessage(message)
    }
}
