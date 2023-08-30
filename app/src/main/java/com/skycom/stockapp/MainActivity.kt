package com.skycom.stockapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.skycom.stockapp.ui.theme.StockAppTheme

class MainActivity : ComponentActivity() {

    private val viewModel: StockViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StockAppTheme {
                StockScreen(viewModel = viewModel)
            }
        }
    }

    @Composable
    fun StockItem(stock: StockData) {
        val color: Color
        val sign: String
        if (stock.changePercentage >= 0) {
            color = MaterialTheme.colorScheme.secondary
            sign = "+"
        } else {
            color = MaterialTheme.colorScheme.error
            sign = "-"
        }
        val priceColor =
            if(stock.currentPrice >= stock.lastPrice)
                MaterialTheme.colorScheme.secondary
            else
                MaterialTheme.colorScheme.error

        Surface(
            modifier = Modifier
                .fillMaxWidth(),
            shadowElevation = 1.dp,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically

            ) {
                Text(
                    text = stock.symbol,
                    modifier = Modifier.fillMaxWidth(0.4F)
                )
                Text(
                    text = "$${stock.currentPrice}",
                    color = priceColor
                )
                Text(
                    text = "$sign${stock.changePercentage}%",
                    color = color
                )
            }
        }
    }

    @Composable
    fun StockScreen(viewModel: StockViewModel) {
        StockList(viewModel)
    }

    @Composable
    fun StockList(viewModel: StockViewModel) {
        val stockData = viewModel.stockData

        Surface() {
            LazyColumn(
                Modifier
                    .padding(top = 32.dp, bottom = 16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(
                    items = stockData,
                    key = { it.symbol }
                ) { stock ->
                    StockItem(stock)
                }
            }
        }
    }
}
