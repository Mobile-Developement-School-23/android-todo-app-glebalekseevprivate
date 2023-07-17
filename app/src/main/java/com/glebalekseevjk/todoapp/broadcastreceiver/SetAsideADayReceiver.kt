package com.glebalekseevjk.todoapp.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.glebalekseevjk.domain.todoitem.repository.TodoItemRepository
import com.glebalekseevjk.todoapp.broadcastreceiver.NotificationReceiver.Companion.TODOITEM_ID
import com.glebalekseevjk.todoapp.utils.appComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject


class SetAsideADayReceiver : BroadcastReceiver() {

    @Inject
    lateinit var todoItemRepository: TodoItemRepository

    override fun onReceive(context: Context, intent: Intent) {
        context.appComponent.inject(this)
        val todoItemId = intent.getStringExtra(TODOITEM_ID)
        todoItemId ?: throw NoSuchElementException("Bad todoItemId from extra")
        CoroutineScope(Dispatchers.Default).launch {
            val todoItem = todoItemRepository.getTodoItemByIdOrNull(todoItemId)
            todoItem ?: throw NoSuchElementException()
            todoItemRepository.updateTodoItem(
                todoItem.copy(
                    deadline = todoItem.deadline.let {
                        val calendar = Calendar.getInstance()
                        calendar.add(Calendar.DAY_OF_MONTH, 1)
                        calendar.time
                    }
                )
            )
        }
    }
}