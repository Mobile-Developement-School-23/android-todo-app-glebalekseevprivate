package com.glebalekseevjk.todoapp.di.module

import com.glebalekseevjk.data.auth.AuthRepositoryImpl
import com.glebalekseevjk.data.sync.SynchronizationRepositoryImpl
import com.glebalekseevjk.domain.auth.AuthRepository
import com.glebalekseevjk.domain.sync.SynchronizationRepository
import com.glebalekseevjk.domain.sync.SynchronizationSchedulerManager
import com.glebalekseevjk.todoapp.di.scope.AppComponentScope
import com.glebalekseevjk.todoapp.worker.SynchronizationSchedulerManagerImpl
import dagger.Binds
import dagger.Module

@Module
interface RepositoryModule {
    @AppComponentScope
    @Binds
    fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @AppComponentScope
    @Binds
    fun bindSynchronizationRepository(
        synchronizationRepositoryImpl: SynchronizationRepositoryImpl
    ): SynchronizationRepository

    @AppComponentScope
    @Binds
    fun bindSynchronizationSchedulerManager(
        synchronizationSchedulerManagerImpl: SynchronizationSchedulerManagerImpl
    ): SynchronizationSchedulerManager
}