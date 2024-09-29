package com.xm.xapi.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * Describe:
 */
open class ApiViewModel : ViewModel() {

    fun <T> launch(block: suspend () -> T, error: (Throwable) -> Unit) =
        viewModelScope.launch {
            try {
                block()
            } catch (e: Throwable) {
                e.printStackTrace()
                error(e)
            }
        }

    fun <T> launch(block: suspend () -> T) = viewModelScope.launch {
        try {
            block()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

}