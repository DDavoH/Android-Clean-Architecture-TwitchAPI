package com.davoh.oauth2_twitch.di

import android.app.Application

class MainApplication:Application() {
    val appComponent: ApplicationComponent = DaggerApplicationComponent.factory().create(this)
}