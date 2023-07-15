package com.glebalekseevjk.core.room.mapper

import com.glebalekseevjk.common.Mapper
import com.glebalekseevjk.core.room.model.EventNotificationDbModel
import com.glebalekseevjk.domain.todoitem.entity.EventNotification
import javax.inject.Inject

class EventNotificationMapperImpl @Inject constructor() :
    Mapper<EventNotification, EventNotificationDbModel> {
    override fun mapItemToAnotherItem(item: EventNotification): EventNotificationDbModel =
        EventNotificationDbModel(
            item.id,
            item.todoId,
            item.date,
        )

    override fun mapAnotherItemToItem(dbModel: EventNotificationDbModel): EventNotification =
        EventNotification(
            dbModel.id,
            dbModel.todoId,
            dbModel.date,
        )
}