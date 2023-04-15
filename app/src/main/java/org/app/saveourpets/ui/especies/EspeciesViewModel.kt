package org.app.saveourpets.ui.especies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EspeciesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is species 1 Fragment"
    }
    val text: LiveData<String> = _text
}