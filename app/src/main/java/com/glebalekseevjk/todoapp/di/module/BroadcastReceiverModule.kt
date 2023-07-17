package com.glebalekseevjk.todoapp.di.module

import com.glebalekseevjk.todoapp.broadcastreceiver.NotificationReceiver
import dagger.Module
import dagger.Provides
import di.NotificationReceiverClass

@Module
interface BroadcastReceiverModule {
    companion object {
        @Provides
        @NotificationReceiverClass
        fun provideNotificationReceiverClass(): Class<*> {
            return NotificationReceiver::class.java
        }
    }
}