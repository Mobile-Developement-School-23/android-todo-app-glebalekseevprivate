package com.glebalekseevjk.core.preferences

import java.util.Date

interface PersonalStorage {
    var revision: String?
    var token: String?
    var lastSynchronizationDate: Date
    var deviceId: String
    fun clear()
}