package ua.shtain.opensky.core.navigator.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.shtain.opensky.core.navigator.NavigatorApi
import ua.shtain.opensky.core.navigator.NavigatorImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigatorModule {

    @Binds
    abstract fun provideNavigator(navigator: NavigatorImpl): NavigatorApi
}