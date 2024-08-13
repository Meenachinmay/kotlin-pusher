package com.meenachinmay.chatapp.controller

import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.server.ServerWebExchange
import kotlinx.coroutines.reactive.awaitSingle
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value

@RestController
class PusherController {

    private val logger = LoggerFactory.getLogger(PusherController::class.java)
    @Value("\${pusher.app-key}")
    private lateinit var appKey: String

    @Value("\${pusher.app-secret}")
    private lateinit var appSecret: String

    @PostMapping("/pusher/auth", consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    suspend fun authenticatePusherUser(serverWebExchange: ServerWebExchange): ResponseEntity<String> {
        val formData = serverWebExchange.formData.awaitSingle()
        val socketId = formData["socket_id"]?.firstOrNull()
        val channelName = formData["channel_name"]?.firstOrNull()

        logger.info("Received auth request - socket_id: $socketId, channel_name: $channelName")

        if (socketId == null) {
            logger.error("Missing socket_id")
            return ResponseEntity.badRequest().body("Missing socket_id")
        }
        if (channelName == null) {
            logger.error("Missing channel_name")
            return ResponseEntity.badRequest().body("Missing channel_name")
        }

        val stringToSign = "$socketId:$channelName"
        val signature = generateHmacSha256(appSecret, stringToSign)
        val auth = "$appKey:$signature"

        val jsonResponse = """
            {
              "auth": "$auth"
            }
        """.trimIndent()

        logger.info("Sending auth response: $jsonResponse")
        return ResponseEntity.ok(jsonResponse)
    }

    private fun generateHmacSha256(key: String, data: String): String {
        val algorithm = "HmacSHA256"
        val secretKeySpec = SecretKeySpec(key.toByteArray(), algorithm)
        val mac = Mac.getInstance(algorithm)
        mac.init(secretKeySpec)
        val hashBytes = mac.doFinal(data.toByteArray())
        return hashBytes.joinToString("") { "%02x".format(it) }
    }
}