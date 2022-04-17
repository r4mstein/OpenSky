package ua.shtain.opensky.core.model_converters.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ua.shtain.opensky.core.model_converters.StatesConverter
import ua.shtain.opensky.core.model_converters.StatesConverterApi

@Module
@InstallIn(SingletonComponent::class)
abstract class ConvertersModule {

    @Binds
    abstract fun provideStatesConverter(converter: StatesConverter): StatesConverterApi
}