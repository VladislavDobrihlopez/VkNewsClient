package com.voitov.vknewsclient.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.voitov.vknewsclient.di.scopes.ApplicationScope
import javax.inject.Inject

class ViewModelsFactory @Inject constructor(
    private val map: @JvmSuppressWildcards Map<String, ViewModel>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        Log.d("VIEWMODELS_FACTORY", this.toString())
        return map[modelClass.simpleName] as T
    }
}