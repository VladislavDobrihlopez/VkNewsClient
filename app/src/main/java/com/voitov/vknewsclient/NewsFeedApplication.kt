package com.voitov.vknewsclient

import android.app.Application
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.voitov.vknewsclient.di.components.ApplicationComponent
import com.voitov.vknewsclient.di.components.DaggerApplicationComponent

class NewsFeedApplication : Application() {
    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}

@Composable
fun getApplicationComponent(): ApplicationComponent {
    Log.d("NEWS_FEED_APPLICATIONN", "getApplication")
    return (LocalContext.current.applicationContext as NewsFeedApplication).component
}