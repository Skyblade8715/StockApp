package com.skycom.stockapp

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

object StocksHolder {
    var stocksList: MutableList<StockData> = mutableListOf()

    init {
        StockList.stockSymbols.forEach {
            x -> stocksList.add(StockData(x))
        }
    }

    fun updateStocks(serverMessage: String){

        val tradeData = Json.decodeFromString<TradeData>(serverMessage)
        tradeData.data.forEach { stock -> run {
                stocksList.find { it.symbol == stock.s }?.currentPrice = stock.p
            }
        }
    }
}
