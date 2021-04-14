package com.davoh.oauth2_twitch.di

import android.app.Application

class MainApplication:Application() {
    private lateinit var component:ApplicationComponent
    override fun onCreate() {
        super.onCreate()
        component = DaggerApplicationComponent.create()
    }

    fun getComponent():ApplicationComponent{
        return component
    }
}