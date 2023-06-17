package com.glebalekseevjk.todoapp.presentation.rv.callback

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.view.View
import androidx.core.math.MathUtils
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE
import androidx.recyclerview.widget.ItemTouchHelper.Callback
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.glebalekseevjk.todoapp.presentation.rv.adapter.TodoItemsAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.LinkedList
import kotlin.math.abs

class SwipeCallback constructor(private val scrollConstraintOffset: Float) : Callback() {
    private var isFirst = false
    private var startScrollX = 0
    private var boundaryScrollX = 0
    private var boundaryOffset = 0f
    private val viewHolderItemIdList: LinkedList<View> = LinkedList()
    private var valueAnimator: ValueAnimator? = null

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val currentList = (recyclerView.adapter!! as TodoItemsAdapter).currentList
        if (viewHolder.adapterPosition == currentList.size) return 0
        val item = currentList.get(viewHolder.adapterPosition)
        val contain = viewHolderItemIdList.contains(viewHolder.itemView)
        return makeMovementFlags(
            0,
            if (item.isDone && !contain) LEFT else LEFT or RIGHT
        )
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        return Float.MAX_VALUE
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return Float.MAX_VALUE
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ACTION_STATE_SWIPE) {
            if (dX == 0f) {
                isFirst = true
                startScrollX = viewHolder.itemView.scrollX
                if (!viewHolderItemIdList.contains(viewHolder.itemView)) {
                    viewHolderItemIdList.add(viewHolder.itemView)
                }
                viewHolderItemIdList.forEach { view ->
                    if (view != viewHolder.itemView) {
                        CoroutineScope(Dispatchers.Main).launch {
                            ValueAnimator.ofFloat(
                                getScrollOffsetWithConstraint(
                                    boundaryScrollX,
                                    0f
                                ).toFloat(), 0f
                            ).apply {
                                duration = 200
                                addUpdateListener {
                                    view.scrollTo((it.animatedValue as Float).toInt(), 0)
                                }
                                start()
                            }
                        }
                        viewHolderItemIdList.remove(view)
                    }
                }
            }
            valueAnimator?.let { if (it.isRunning) return }

            if (isCurrentlyActive) {
                val item =
                    (recyclerView.adapter!! as TodoItemsAdapter).currentList.get(viewHolder.adapterPosition)
                if (!(item.isDone && (startScrollX - dX) < 0)) {
                    viewHolder.itemView.scrollTo((startScrollX - dX).toInt(), 0)
                }
            } else {
                if (isFirst) {
                    isFirst = false
                    boundaryScrollX = viewHolder.itemView.scrollX
                    boundaryOffset = dX
                }

                val scroll = abs(viewHolder.itemView.scrollX)
                if (scroll in 1 until scrollConstraintOffset.toInt()) {
                    viewHolder.itemView.scrollTo(
                        (boundaryScrollX * dX / boundaryOffset).toInt(),
                        0
                    )
                }
                if (scroll > scrollConstraintOffset.toInt()) {
                    val scrollOffset = getScrollOffsetWithConstraint(startScrollX, dX)
                    valueAnimator =
                        ValueAnimator.ofFloat(boundaryScrollX.toFloat(), scrollOffset.toFloat())
                    CoroutineScope(Dispatchers.Main).launch {
                        valueAnimator!!.apply {
                            duration = 150
                            addUpdateListener {
                                viewHolder.itemView.scrollTo((it.animatedValue as Float).toInt(), 0)
                            }
                            start()
                        }
                    }
                }
            }
        }
    }

    private fun getScrollOffsetWithConstraint(startScrollX: Int, dX: Float): Int {
        val scrollWithOffset = startScrollX - dX
        return MathUtils.clamp(scrollWithOffset, -scrollConstraintOffset, scrollConstraintOffset)
            .toInt()
    }

    fun resetViewHolder(viewHolder: ViewHolder) {
        viewHolderItemIdList.forEach { view ->
            if (view == viewHolder.itemView) {
                CoroutineScope(Dispatchers.Main).launch {
                    ValueAnimator.ofFloat(
                        getScrollOffsetWithConstraint(
                            boundaryScrollX,
                            0f
                        ).toFloat(), 0f
                    ).apply {
                        duration = 200
                        addUpdateListener {
                            view.scrollTo((it.animatedValue as Float).toInt(), 0)
                        }
                        start()
                    }
                }
                viewHolderItemIdList.remove(view)
            }
        }
    }
}