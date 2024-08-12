package com.meenachinmay.chatapp.model

data class User(val id: String, val name: String)

data class Message(val from: String, val to: String, val content: String)

data class ChatRoom(val id: String, val participants: List<String>)