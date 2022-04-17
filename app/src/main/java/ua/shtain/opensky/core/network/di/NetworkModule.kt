package ua.shtain.opensky.core.network.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.shtain.opensky.core.network.http_client.HttpClientApi
import ua.shtain.opensky.core.network.http_client.HttpClientImpl
import ua.shtain.opensky.core.network.network_manager.NetworkManagerApi
import ua.shtain.opensky.core.network.network_manager.NetworkManagerImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    @Binds
    abstract fun provideHttpClient(httpClient: HttpClientImpl): HttpClientApi

    @Binds
    abstract fun provideNetworkManager(manager: NetworkManagerImpl): NetworkManagerApi
}