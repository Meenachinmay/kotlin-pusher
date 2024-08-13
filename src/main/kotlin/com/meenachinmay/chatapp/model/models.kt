package com.meenachinmay.chatapp.model

data class User(val name: String)

data class Message(val body: String, val to: String, val from: String)

data class ChatRoom(val id: String, val participants: List<String>)