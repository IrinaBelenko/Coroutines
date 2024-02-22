package com.example.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyViewModel : ViewModel() {
    private val _uiState = MutableLiveData<UIState>(UIState.Empty)
    val uiState: LiveData<UIState> = _uiState
    private val repo = MyApplication.getApp().repo

    fun getData() {
        _uiState.value = UIState.Processing
        viewModelScope.launch(Dispatchers.IO) {
            var result = ""
            val bitcoin = async { repo.getCurrencyByName("bitcoin") }.await()
            val ounce = async { repo.getCurrencyByName("silver-ounce") }.await()
            val dash = async { repo.getCurrencyByName("dash") }.await()
            val binance = async { repo.getCurrencyByName("binance-coin") }.await()

            if (bitcoin?.data != null) {
                result += "${bitcoin.data.id} ${bitcoin.data.rateUsd}\n"
            }
            if (ounce?.data != null) {
                result += "${ounce.data.id} ${ounce.data.rateUsd}\n"
            }
            if (dash?.data != null) {
                result += "${dash.data.id} ${dash.data.rateUsd}\n"
            }
            if (binance?.data != null) {
                result += "${binance.data.id} ${binance.data.rateUsd}"
            }
            _uiState.postValue(UIState.Result(result))
        }
    }

    sealed class UIState {
        object Empty : UIState()
        object Processing : UIState()
        class Result(val title: String) : UIState()
        class Error(val description: String) : UIState()
    }
}
