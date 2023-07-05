package com.glebalekseevjk.todoapp.ioc.module

import android.util.Log
import com.glebalekseevjk.todoapp.Constants
import com.glebalekseevjk.todoapp.data.datasource.remote.TodoItemService
import com.glebalekseevjk.todoapp.data.datasource.remote.interceptor.AuthorizationFailedInterceptor
import com.glebalekseevjk.todoapp.data.datasource.remote.interceptor.AuthorizationInterceptor
import com.glebalekseevjk.todoapp.data.datasource.remote.interceptor.GenerateFailsInterceptor
import com.glebalekseevjk.todoapp.data.datasource.remote.interceptor.RevisionInterceptor
import com.glebalekseevjk.todoapp.data.datasource.remote.interceptor.SaveRevisionInterceptor
import com.glebalekseevjk.todoapp.ioc.scope.AppComponentScope
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
interface RemoteDataSourceModule {
    companion object {
        @Provides
        fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
            return HttpLoggingInterceptor {
                Log.d("Network", it)
            }
                .setLevel(HttpLoggingInterceptor.Level.BODY)
        }

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
            authorizationFailedInterceptor: AuthorizationFailedInterceptor,
            revisionInterceptor: RevisionInterceptor,
            saveRevisionInterceptor: SaveRevisionInterceptor,
            generateFailsInterceptor: GenerateFailsInterceptor,
            loggingInterceptor: HttpLoggingInterceptor,
        ): OkHttpClient {
            return OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addNetworkInterceptor(authorizationFailedInterceptor)
                .addNetworkInterceptor(saveRevisionInterceptor)
//                .addNetworkInterceptor(generateFailsInterceptor)
                .addInterceptor(authorizationInterceptor)
                .addInterceptor(revisionInterceptor)
                .addNetworkInterceptor(loggingInterceptor)
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