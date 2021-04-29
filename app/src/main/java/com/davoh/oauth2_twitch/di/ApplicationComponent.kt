package com.davoh.oauth2_twitch.di

import com.davoh.oauth2_twitch.MainActivity
import com.davoh.oauth2_twitch.data.di.RepositoryModule
import com.davoh.oauth2_twitch.framework.requestmanager.di.APIModule
import com.davoh.oauth2_twitch.ui.TopGamesFragment
import com.davoh.oauth2_twitch.usecases.di.UseCaseModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [APIModule::class, RepositoryModule::class, UseCaseModule::class])
interface ApplicationComponent {

    fun inject(activity: MainActivity)
    fun inject(topGamesFragment: TopGamesFragment)
}