package com.meenachinmay.chatapp.service

import com.meenachinmay.chatapp.model.Message
import com.meenachinmay.chatapp.model.User
import com.pusher.rest.Pusher
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap
import org.slf4j.LoggerFactory

@Service
class ChatService(private val pusher: Pusher) {
    private val logger = LoggerFactory.getLogger(ChatService::class.java)
    private val _users: ConcurrentHashMap<String, User> = ConcurrentHashMap()
    val users: Map<String, User> get() = _users

    fun addUser(user: User): User {
        if (user.name.isBlank()) {
            throw IllegalArgumentException("User name cannot be null or blank")
        }
        _users[user.name] = user
        logger.info("User added: $user")
        pusher.trigger("new-login", "user-connected", user)
        return user
    }

    fun removeUser(user: User) {
        if (user.name.isBlank()) {
            throw IllegalArgumentException("User name cannot be null or blank")
        }
        _users.remove(user.name)
        logger.info("User removed: $user")
        pusher.trigger("new-login", "user-disconnected", user)
    }

    fun getUsers(): List<User> {
        logger.info("Fetching all users. Current count: ${_users.size}")
        return _users.values.toList()
    }

    fun sendMessage(message: Message) {
        logger.info("Sending message: $message")
        val channelName = "private-chat-${message.to}"
        pusher.trigger(channelName, "new-message", message)
    }
}