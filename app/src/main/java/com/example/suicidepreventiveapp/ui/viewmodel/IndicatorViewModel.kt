package com.example.suicidepreventiveapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class IndicatorViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is indicator Fragment"
    }
    val text: LiveData<String> = _text
}