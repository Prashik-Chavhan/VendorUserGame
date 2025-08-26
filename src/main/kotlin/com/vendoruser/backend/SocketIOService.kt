package com.vendoruser.backend

import com.corundumstudio.socketio.Configuration
import com.corundumstudio.socketio.SocketIOServer
import jakarta.annotation.PostConstruct
import jakarta.annotation.PreDestroy
import org.springframework.stereotype.Component

@Component
class SocketIOService(private val repository: GeneratedNumberRepository) {

    lateinit var server: SocketIOServer

    @PostConstruct
    fun startServer() {
        val config = Configuration().apply {
            port = 9092
            bossThreads = 1
            workerThreads = 2
        }

        server = SocketIOServer(config)

        server.addEventListener("generate_number", String::class.java) { _, gameId, _ ->
            val number = (0..99).random()
            val record = GeneratedNumber(number = number, gameId = gameId)
            repository.save(record)
            println("Generated number $number for game $gameId")

            server.broadcastOperations.sendEvent(
                "receive_number",
                mapOf(
                    "number" to number,
                    "gameId" to gameId
                )
            )
        }

        server.start()
        println("Socket.IO server started on port 9092")
    }

    @PreDestroy
    fun stopServer() {
        server.stop()
        println("Socket.IO server stopped")
    }
}