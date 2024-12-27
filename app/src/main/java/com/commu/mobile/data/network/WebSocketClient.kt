package com.commu.mobile.data.network

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.websocket.Frame
import io.ktor.websocket.readText
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class WebSocketClient {

    private val client = HttpClient {
        install(WebSockets)
    }
    private val _messages = MutableSharedFlow<String>()
    val messages = _messages.asSharedFlow()
    private var session: DefaultClientWebSocketSession? = null
    suspend fun connect(url: String) {
        client.webSocket(url) {
            session = this
            Log.i("Websocket", "Websocket connection successfully established")

            // Listening for incoming messages
            for (frame in incoming) {
                if (frame is Frame.Text) {
                    _messages.emit(frame.readText())
                }
            }

        }
    }

    suspend fun sendMessage(message: String, url: String) {
        Log.i("Websocket", "Message sent")
        client.webSocket(url) {
            send(Frame.Text(message))
        }
    }

    fun close() {
        client.close()
    }

}
