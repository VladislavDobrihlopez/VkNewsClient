package com.voitov.vknewsclient

import android.app.Application
import com.voitov.vknewsclient.di.components.ApplicationComponent
import com.voitov.vknewsclient.di.components.DaggerApplicationComponent

class NewsFeedApplication : Application() {
    val component: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}