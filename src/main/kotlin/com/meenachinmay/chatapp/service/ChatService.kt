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
    private val users = ConcurrentHashMap<String, User>()

    fun addUser(user: User): User {
        users[user.name] = user
        logger.info("User added: $user")
        pusher.trigger("new-login", "user-connected", user)
        return user
    }

    fun removeUser(user: User) {
        users.remove(user.name)
        logger.info("User removed: $user")
        pusher.trigger("new-login", "user-disconnected", user)
    }

    fun getUsers(): List<User> {
        logger.info("Fetching all users. Current count: ${users.size}")
        return users.values.toList()
    }

    fun sendMessage(message: Message) {
        logger.info("Sending message: $message")
        val channelName = "private-chat-${message.to}"
        pusher.trigger(channelName, "new-message", message)
    }

}