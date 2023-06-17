package com.glebalekseevjk.todoapp.presentation.rv.adapter

import android.content.Context
import android.graphics.Paint
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.glebalekseevjk.todoapp.R
import com.glebalekseevjk.todoapp.databinding.NewTodoItemRvBinding
import com.glebalekseevjk.todoapp.databinding.TodoItemRvBinding
import com.glebalekseevjk.todoapp.domain.entity.TodoItem
import com.glebalekseevjk.todoapp.domain.entity.TodoItem.Companion.Importance.IMPORTANT
import com.glebalekseevjk.todoapp.domain.entity.TodoItem.Companion.Importance.LOW
import com.glebalekseevjk.todoapp.presentation.rv.callback.TodoItemDiffCallBack
import com.glebalekseevjk.todoapp.presentation.viewmodel.TodoItemsAction
import com.glebalekseevjk.todoapp.utils.getColorFromTheme
import com.glebalekseevjk.todoapp.utils.getMarginSpan
import java.text.SimpleDateFormat
import java.util.Locale


class TodoItemsAdapter :
    ListAdapter<TodoItem, ViewHolder>(TodoItemDiffCallBack()) {
    var editClickListener: ((todoId: String)->Unit)? = null
    var changeDoneStatusClickListener: ((todoId: String)->Unit)? = null
    var setDoneStatusClickListener: ((todoId: String, viewHolder: ViewHolder)->Unit)? = null
    var addTodoItemClickListener: (()->Unit)? = null
    var deleteTodoItemClickListener: ((todoId: String, viewHolder: ViewHolder)->Unit)? = null

    inner class TaskItemViewHolder(val binding: TodoItemRvBinding) :
        ViewHolder(binding.root)

    inner class FooterItemViewHolder(val binding: NewTodoItemRvBinding) :
        ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1) VIEW_TYPE_FOOTER
        else VIEW_TYPE_ITEM
    }

    override fun getItemCount(): Int = this.currentList.size + 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        when (viewType) {
            VIEW_TYPE_ITEM -> {
                val binding = TodoItemRvBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                TaskItemViewHolder(binding).also {
                    println("============= onCreateViewHolder viewolder = $it")
                }
            }

            VIEW_TYPE_FOOTER -> {
                val binding = NewTodoItemRvBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                FooterItemViewHolder(binding)
            }

            else -> throw RuntimeException("Unknown view type")
        }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        println("============= onBindViewHolder holder=$holder position+$position")

        when(holder){
            is TaskItemViewHolder ->{
                val todoItem = getItem(position)
                val binding = holder.binding
                val context = binding.textTv.context

                binding.textTv.text = getTextWithImportancePrefix(context, todoItem)
                checkIsDoneText(context, todoItem, binding)
                checkIsDoneCheckBox(context, todoItem, binding)
                checkDeadline(todoItem, binding)
                binding.infoIb.setOnClickListener {
                    editClickListener?.invoke(todoItem.id)
                }
                binding.isDoneCb.setOnClickListener {
                    changeDoneStatusClickListener?.invoke(todoItem.id)
                }
                binding.deleteIb.setOnClickListener {
                    deleteTodoItemClickListener?.invoke(todoItem.id, holder)
                }
                binding.setDoneIb.setOnClickListener {
                    setDoneStatusClickListener?.invoke(todoItem.id, holder)
                }
            }
            is FooterItemViewHolder ->{
                val binding = holder.binding
                binding.newTodoTv.setOnClickListener{
                    addTodoItemClickListener?.invoke()
                }
            }
        }
    }

    private fun getTextWithImportancePrefix(
        context: Context,
        todoItem: TodoItem
    ): SpannableStringBuilder {
        val spannableString = SpannableStringBuilder()
        val colorLow = context.getColorFromTheme(R.attr.color_gray)
        val colorImportant = context.getColorFromTheme(R.attr.color_red)
        when (todoItem.importance) {
            LOW -> {
                spannableString.append(
                    String.format(context.getString(R.string.low_symbol), todoItem.text)
                )
                spannableString.setSpan(
                    ForegroundColorSpan(colorLow),
                    0,
                    1,
                    SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                spannableString.setSpan(
                    getMarginSpan(1, -10),
                    0,
                    1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );
            }

            IMPORTANT -> {
                spannableString.append(
                    String.format(context.getString(R.string.important_symbol), todoItem.text)
                )
                spannableString.setSpan(
                    ForegroundColorSpan(colorImportant),
                    0,
                    2,
                    SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }

            else -> spannableString.append(todoItem.text)
        }
        return spannableString
    }

    private fun checkIsDoneText(context: Context, todoItem: TodoItem, binding: TodoItemRvBinding) {
        val colorLabelTertiary = context.getColorFromTheme(R.attr.label_tertiary)
        val colorLabelPrimary = context.getColorFromTheme(R.attr.label_primary)
        if (todoItem.isDone) {
            binding.textTv.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            binding.textTv.setTextColor(colorLabelTertiary)
        } else {
            binding.textTv.paintFlags = 1283
            binding.textTv.setTextColor(colorLabelPrimary)
        }
    }

    private fun checkIsDoneCheckBox(
        context: Context,
        todoItem: TodoItem,
        binding: TodoItemRvBinding
    ) {
        val drawable = if (todoItem.importance == IMPORTANT) {
            AppCompatResources.getDrawable(context, R.drawable.important_checkbox_selector)
        } else {
            AppCompatResources.getDrawable(context, R.drawable.default_checkbox_selector)
        }
        binding.isDoneCb.background = drawable
        binding.isDoneCb.isChecked = todoItem.isDone
    }

    private fun checkDeadline(
        todoItem: TodoItem,
        binding: TodoItemRvBinding
    ) {
        if (todoItem.deadline == null) {
            binding.deadlineTv.visibility = View.GONE
        } else {
            val formatter = SimpleDateFormat("dd MMMM yyyy", Locale("ru"))
            binding.deadlineTv.text = formatter.format(todoItem.deadline)
            binding.deadlineTv.visibility = View.VISIBLE
        }
    }

    companion object {
        const val VIEW_TYPE_ITEM = 0
        const val VIEW_TYPE_FOOTER = 1
    }
}

