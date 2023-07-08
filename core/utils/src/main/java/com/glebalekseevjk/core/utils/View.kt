package com.glebalekseevjk.core.utils

import android.R
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.text.Layout
import android.text.style.LeadingMarginSpan
import android.util.TypedValue

fun Context.getColorFromTheme(attr: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attr, typedValue, true)
    return typedValue.data
}

fun getMarginSpan(linesCount: Int, margin: Int) = object :
    LeadingMarginSpan.LeadingMarginSpan2 {
    override fun getLeadingMargin(first: Boolean): Int {
        return if (first) margin else 0
    }

    override fun drawLeadingMargin(
        c: Canvas?,
        p: Paint?,
        x: Int,
        dir: Int,
        top: Int,
        baseline: Int,
        bottom: Int,
        text: CharSequence?,
        start: Int,
        end: Int,
        first: Boolean,
        layout: Layout?
    ) {
        if (first) {
            val rect = Rect(
                R.attr.x + dir * margin,
                R.attr.top,
                R.attr.x + dir * margin,
                R.attr.bottom
            )
            val rectPaint = Paint()
            c!!.drawRect(rect, rectPaint)
        }
    }

    override fun getLeadingMarginLineCount(): Int {
        return linesCount
    }
}
