package com.glebalekseevjk.todoapp.ioc.module

import com.glebalekseevjk.todoapp.Constants
import com.glebalekseevjk.todoapp.data.datasource.remote.TodoItemService
import com.glebalekseevjk.todoapp.data.datasource.remote.interceptor.AuthorizationInterceptor
import com.glebalekseevjk.todoapp.data.datasource.remote.interceptor.RevisionInterceptor
import com.glebalekseevjk.todoapp.ioc.scope.AppComponentScope
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
interface RemoteDataSourceModule {
    companion object {
        @AppComponentScope
        @Provides
        fun provideRetrofitBuilder(): Retrofit.Builder {
            return Retrofit.Builder()
                .baseUrl(Constants.API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
        }

        @AppComponentScope
        @Provides
        fun provideOkHttpClient(
            authorizationInterceptor: AuthorizationInterceptor,
            revisionInterceptor: RevisionInterceptor
        ): OkHttpClient {
            return OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addNetworkInterceptor(authorizationInterceptor)
                .addNetworkInterceptor(revisionInterceptor)
                .build()
        }

        @AppComponentScope
        @Provides
        fun provideAuthService(
            retrofitBuilder: Retrofit.Builder,
            okHttpClient: OkHttpClient
        ): TodoItemService {
            return retrofitBuilder
                .client(okHttpClient)
                .build()
                .create(TodoItemService::class.java)
        }
    }
}