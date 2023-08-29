package com.skycom.stockapp

import io.finnhub.api.apis.DefaultApi
import io.finnhub.api.infrastructure.ApiClient
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

data class StockData (
    val symbol: String,
) {
    var openingPrice: Double = 0.0
    var changePercentage: Double = 0.0
    var currentPrice: Double = 0.0
        set(value) {
            field = value.roundDecimals()
            changePercentage = (value/openingPrice).roundDecimals()
        }

    init {
        ApiClient.apiKey["token"] = "cjjpajpr01qorp963ml0cjjpajpr01qorp963mlg"
        val apiClient = DefaultApi()
        val quote = apiClient.quote(symbol)
        openingPrice = quote.o?.toDouble() ?: -1.0
        currentPrice = quote.c?.toDouble()?.roundDecimals() ?: -1.0
        changePercentage = (currentPrice/openingPrice).roundDecimals()
    }

    private fun Double.roundDecimals() : Double{
        return String.format("%.2f", this).toDouble()
    }
}