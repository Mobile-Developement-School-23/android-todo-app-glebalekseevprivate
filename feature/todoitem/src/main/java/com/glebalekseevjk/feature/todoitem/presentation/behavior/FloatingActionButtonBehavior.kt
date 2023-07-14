package com.glebalekseevjk.feature.todoitem.presentation.behavior

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import java.lang.Float.min

/**
Ответственность класса FloatingActionButtonBehavior:
Класс FloatingActionButtonBehavior предоставляет
поведение плавающей кнопки действия (FloatingActionButton) в CoordinatorLayout.
Он определяет, зависит ли расположение кнопки от другого представления (Snackbar.SnackbarLayout),
и управляет изменением позиции кнопки, когда это представление изменяется или удаляется.
Класс отвечает за корректное отображение
и анимацию кнопки действия в зависимости от других элементов интерфейса.
 */
class FloatingActionButtonBehavior(context: Context, attrs: AttributeSet) :
    CoordinatorLayout.Behavior<FloatingActionButton>() {
    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: FloatingActionButton,
        dependency: View
    ): Boolean {
        return dependency is Snackbar.SnackbarLayout
    }

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: FloatingActionButton,
        dependency: View
    ): Boolean {
        val translationY =
            min(0f, dependency.translationY - dependency.height)
        child.translationY = translationY
        return true
    }

    override fun onDependentViewRemoved(
        parent: CoordinatorLayout,
        child: FloatingActionButton,
        dependency: View
    ) {
        ObjectAnimator.ofFloat(child, "translationY", 0f).apply {
            duration = 200
            interpolator = AccelerateInterpolator()
            start()
        }
        super.onDependentViewRemoved(parent, child, dependency)
    }
}