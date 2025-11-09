package edu.ucne.ramon_lopez_ap2_p2.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.ucne.ramon_lopez_ap2_p2.data.remote.repository.GastoRepositoryImpl
import edu.ucne.ramon_lopez_ap2_p2.domain.repository.GastoRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindGastoRepository(
        impl: GastoRepositoryImpl
    ): GastoRepository
}