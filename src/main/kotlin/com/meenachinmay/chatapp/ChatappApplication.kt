package com.meenachinmay.chatapp

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication  // Adjust this package name to match your project structure
class ChatappApplication

fun main(args: Array<String>) {
	runApplication<ChatappApplication>(*args)
}
