package com.skycom.stockapp

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class StockDataAPI (val viewModel: StockViewModel){
    private val client = OkHttpClient()
    private val url = "wss://ws.finnhub.io?token=cjjpajpr01qorp963ml0cjjpajpr01qorp963mlg"
    private lateinit var socket: WebSocket

    init {
        connectWebSocket()
    }
    private fun connectWebSocket() {

        val request = Request.Builder().url(url).build()

        socket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                StockList.stockSymbols.forEach {
                    x -> subscribe(x)
                }
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                viewModel.updateStockData(text)
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                println("Closed: $reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                println("Error: ${t.message}")
            }
        })
    }

    private fun subscribe(symbol: String) {
        val json = """{"type":"subscribe", "symbol":"$symbol"}"""
        socket.send(json)
    }

    private fun unsubscribe(symbol: String) {
        val json = """{"type":"unsubscribe", "symbol":"$symbol"}"""
        socket.send(json)
    }

    // Call this method to unsubscribe
    private fun performUnsubscribe(symbol: String) {
        unsubscribe(symbol)
    }
}