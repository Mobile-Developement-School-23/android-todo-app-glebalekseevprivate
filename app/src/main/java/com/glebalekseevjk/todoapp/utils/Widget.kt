package com.glebalekseevjk.todoapp.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.snackbar.Snackbar

fun View.showSnackbar(message: String) {
    val snackbar =
        Snackbar.make(this, message, Toast.LENGTH_SHORT)
    val layoutParams = snackbar.view.layoutParams as CoordinatorLayout.LayoutParams
    layoutParams.setMargins(
        layoutParams.leftMargin,
        layoutParams.topMargin,
        layoutParams.rightMargin,
        layoutParams.bottomMargin + 40
    )
    snackbar.view.layoutParams = layoutParams
    snackbar.show()
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}