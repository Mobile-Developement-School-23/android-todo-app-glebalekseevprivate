package com.glebalekseevjk.todoapp.di.module

import android.util.Log
import com.glebalekseevjk.core.retrofit.TodoItemService
import com.glebalekseevjk.core.retrofit.interceptor.AuthorizationFailedInterceptor
import com.glebalekseevjk.core.retrofit.interceptor.AuthorizationInterceptor
import com.glebalekseevjk.core.retrofit.interceptor.GenerateFailsInterceptor
import com.glebalekseevjk.core.retrofit.interceptor.RevisionInterceptor
import com.glebalekseevjk.core.retrofit.interceptor.SaveRevisionInterceptor
import com.glebalekseevjk.core.utils.Constants
import com.glebalekseevjk.todoapp.di.scope.AppComponentScope
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
        fun provideTodoItemService(
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