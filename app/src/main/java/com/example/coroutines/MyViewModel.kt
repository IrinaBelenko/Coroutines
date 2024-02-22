package com.example.coroutines

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

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

            if (bitcoin.isSuccessful && bitcoin.body() != null) {
                result += "${bitcoin.body()?.data?.id} ${bitcoin.body()?.data?.rateUsd}\n"
            }
            if (ounce.isSuccessful && ounce.body() != null) {
                result += "${ounce.body()?.data?.id} ${ounce.body()?.data?.rateUsd}\n"
            }
            if (dash.isSuccessful && dash.body() != null) {
                result += "${dash.body()?.data?.id} ${dash.body()?.data?.rateUsd}\n"
            }
            if (binance.isSuccessful && binance.body() != null) {
                result += "${binance.body()?.data?.id} ${binance.body()?.data?.rateUsd}\n"
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
