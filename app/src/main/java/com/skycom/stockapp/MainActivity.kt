package com.skycom.stockapp

import android.content.res.Configuration.UI_MODE_NIGHT_YES
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.skycom.stockapp.ui.theme.StockAppTheme
import kotlinx.coroutines.flow.Flow

class MainActivity : ComponentActivity() {

    private val viewModel: StockViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StockAppTheme {
                StockScreen()
            }
        }
    }

    @Composable
    fun StockItem(symbol: String, currentPrice: Double, percentageChange: Double) {
        val color: Color
        val sign: String
        if (percentageChange >= 0) {
            color = MaterialTheme.colorScheme.secondary
            sign = "+"
        } else {
            color = MaterialTheme.colorScheme.error
            sign = "-"
        }

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
                    text = symbol,
                    modifier = Modifier.fillMaxWidth(0.4F)
                )
                Text(text = "$$currentPrice")
                Text(
                    text = "$sign$percentageChange%",
                    color = color
                )
            }
        }
    }

    @Composable
    fun StockScreen() {
        val stockFlow = viewModel.getStocksFlow()

        StockList(stockFlow)
    }

    @Composable
    fun StockList(stockFlow: Flow<List<StockData>>) {
        val stockUpdates by stockFlow.collectAsState(initial = emptyList())

        Surface() {
            LazyColumn(
                Modifier
                    .padding(top = 32.dp, bottom = 16.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(
                    items = stockUpdates,
                    key = { it.symbol }
                ) { stock ->
                    StockItem(stock.symbol, stock.currentPrice, stock.changePercentage)
                }
            }
        }
    }

    @Preview(
        name = "Light mode",
        showBackground = true,
    )
    @Preview(
        name = "Night mode",
        showBackground = true,
        uiMode = UI_MODE_NIGHT_YES
    )
    @Composable
    fun StockItemPreview() {
        StockAppTheme() {
            StockItem("NVDA", 230.0, 0.0)
        }
    }
}
