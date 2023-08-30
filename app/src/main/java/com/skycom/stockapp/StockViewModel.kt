package com.skycom.stockapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import io.finnhub.api.apis.DefaultApi
import io.finnhub.api.infrastructure.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class StockViewModel : ViewModel() {
    var stockData by mutableStateOf(emptyList<StockData>())

    init {
        CoroutineScope(Dispatchers.IO).launch {
            val stocks = mutableListOf<StockData>()
            StockList.stockSymbols.forEach { x ->
                ApiClient.apiKey["token"] = "cjjpajpr01qorp963ml0cjjpajpr01qorp963mlg"
                val apiClient = DefaultApi()
                val quote = apiClient.quote(x)
                val openingPrice = quote.o?.toDouble() ?: -1.0
                val currentPrice = quote.c?.toDouble()?.roundDecimals() ?: -1.0
                val changePercentage = quote.dp?.toDouble()?.roundDecimals() ?: -1.0
                withContext(Dispatchers.Main) {
                    stocks.add(StockData(x, openingPrice, changePercentage, currentPrice, openingPrice))
                }
            }
            stockData = stocks
            StockDataAPI(viewModel = this@StockViewModel)
        }
    }
    fun updateStockData(serverMessage: String) {
        if(serverMessage.contains("data")) {
            val incomingData = Json.decodeFromString<TradeData>(serverMessage)
            val filteredData = incomingData.data
                .groupBy { it.s }
                .mapValues { (_, entries) -> entries.maxByOrNull { it.t }!! }
                .values.toList()

            val filteredTradeData = TradeData(
                data = filteredData,
                type = incomingData.type
            )
            filteredTradeData.data.forEach { filteredStock ->
                run {
                    val indx =
                        stockData.indexOf(stockData.find { x -> x.symbol == filteredStock.s })
                    if(indx != -1) {
                        val currentPrice = stockData[indx].currentPrice
                        val updatedStock = stockData[indx]
                            .copy(
                                currentPrice = filteredStock.p.roundDecimals(),
                                changePercentage =
                                    calculateChangePercentage(filteredStock.p, currentPrice)
                            )
                        stockData = stockData.toMutableList().also { it[indx] = updatedStock }
                    }
                }
            }
        }
    }
    private fun Double.roundDecimals() : Double{
        return String.format("%.2f", this).toDouble()
    }

    private fun calculateChangePercentage(newPrice: Double, oldPrice: Double) : Double{
        return if(newPrice > oldPrice){
            ((newPrice - oldPrice)/newPrice * 100).roundDecimals()
        } else if(newPrice < oldPrice)
            ((oldPrice - newPrice)/oldPrice * 100).roundDecimals()
        else
            0.0
    }
}