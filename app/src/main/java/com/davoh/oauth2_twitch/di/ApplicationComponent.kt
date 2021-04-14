package com.davoh.oauth2_twitch.di

import com.davoh.oauth2_twitch.MainActivity
import com.davoh.oauth2_twitch.framework.RetrofitModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RetrofitModule::class])
interface ApplicationComponent {
    fun inject(target:MainActivity)
}