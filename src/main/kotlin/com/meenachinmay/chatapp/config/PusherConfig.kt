package com.meenachinmay.chatapp.config

import com.pusher.rest.Pusher
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PusherConfig {
    @Value("\${pusher.app-id}")
    private lateinit var appId: String

    @Value("\${pusher.app-key}")
    private lateinit var key: String

    @Value("\${pusher.app-secret}")
    private lateinit var secret: String

    @Value("\${pusher.cluster}")
    private lateinit var cluster: String

    @Bean
    fun pusher(): Pusher {
        return Pusher(appId, key, secret).apply {
            setCluster(cluster)
            setEncrypted(true)
        }
    }
}