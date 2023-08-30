package com.skycom.stockapp

import kotlinx.serialization.Serializable

@Serializable
data class TradeData(
    val data: List<TradeEntry>,
    val type: String
)

@Serializable
data class TradeEntry(
    var c: List<String>,
    var p: Double,
    val s: String,
    var t: Long,
    var v: Int
)

object StockList {
    var stockSymbols : List<String> = listOf(
        "NVDA", "AAPL", "INTC", "AMD", "AMZN", "BYND", "MSFT", "EXCOF"
    )
}


data class StockData(
    val symbol: String,
    var openingPrice: Double = 0.0,
    var changePercentage: Double = 0.0,
    var currentPrice: Double = 0.0,
    var lastPrice: Double = 0.0
) {}