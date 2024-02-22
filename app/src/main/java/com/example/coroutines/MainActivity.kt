package com.example.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val label: TextView = findViewById(R.id.label)
        val button: Button = findViewById(R.id.testB)

        val viewModel = ViewModelProvider(this).get(MyViewModel::class.java)
        button.setOnClickListener { viewModel.getData() }

        viewModel.uiState.observe(this) {
            when (it) {
                is MyViewModel.UIState.Empty -> label.text = "Empty"
                is MyViewModel.UIState.Result -> label.text = it.title
                is MyViewModel.UIState.Processing -> label.text = "Processing..."
                is MyViewModel.UIState.Error -> label.text = it.description
            }
        }
    }

}


data class BitcoinResponse(val data: Data?)

data class Data(
    val id: String,
    val symbol: String,
    val currencySymbol: String,
    val rateUsd: String
)