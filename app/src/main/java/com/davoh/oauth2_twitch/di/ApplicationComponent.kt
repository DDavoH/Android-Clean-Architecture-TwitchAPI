package com.davoh.oauth2_twitch.di

import android.app.Application
import com.davoh.databasemanager.di.DatabaseModule
import com.davoh.oauth2_twitch.MainActivity
import com.davoh.oauth2_twitch.data.di.RepositoryModule
import com.davoh.oauth2_twitch.framework.requestmanager.di.APIModule
import com.davoh.oauth2_twitch.usecases.di.UseCaseModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [APIModule::class, RepositoryModule::class, UseCaseModule::class, DatabaseModule::class])
interface ApplicationComponent {

    fun inject(module: AccessTokenModule): AccessTokenComponent
    fun inject(module:TopGamesModule):TopGamesComponent

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance app: Application): ApplicationComponent
    }

}