package ua.shtain.opensky.core.repositories.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.shtain.opensky.core.repositories.states.StatesRepo
import ua.shtain.opensky.core.repositories.states.StatesRepoApi

@Module
@InstallIn(SingletonComponent::class)
abstract class StatesRepoModule {

    @Binds
    abstract fun provideStatesRepo(repo: StatesRepo): StatesRepoApi
}