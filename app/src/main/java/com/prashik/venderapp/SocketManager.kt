package com.prashik.venderapp

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException

class SocketManager {
    private var socket: Socket? = null

    fun connect() {
        if (socket == null) {
            try {
                val opts = IO.Options().apply {
                    forceNew = true
                    reconnection = true
                    transports = arrayOf("websocket")
                    timeout = 10000
                }

                socket = IO.socket("http://10.0.2.2:9092", opts)

                socket?.on(Socket.EVENT_CONNECT) { Log.d("SocketIO", "Connected") }
                socket?.on(Socket.EVENT_CONNECT_ERROR) { args ->
                    Log.e("SocketIO", "Connect error: ${args.joinToString()}")
                }
                socket?.on(Socket.EVENT_DISCONNECT) { Log.d("SocketIO", "Disconnected") }

            } catch (e: URISyntaxException) {
                e.printStackTrace()
            }
        }
        socket?.connect()
    }

    fun disconnect() {
        socket?.disconnect()
        socket = null
    }

    fun isConnected(): Boolean {
        return socket?.connected() ?: false
    }

    fun sendNumber(number: Int) {
        if (isConnected()) {
            socket?.emit("generate_number", number.toString())
        } else {
            Log.d("SocketIO", "Socket not connected yet")
        }
    }
}