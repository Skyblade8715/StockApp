package com.skycom.stockapp

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow

class StockViewModel : ViewModel() {
    private val stockDataAPI = StockDataAPI()

    fun getStocksFlow(): Flow<List<StockData>> {
        return stockDataAPI.getStocksFlow()
    }
}