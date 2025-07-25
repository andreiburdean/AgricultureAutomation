package com.example.agricultureautomationapp.customviews

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.interpolator.view.animation.FastOutSlowInInterpolator

class SlidingToggleButton @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : View(context, attrs, defStyle) {

    private var isOn = false
    private var sliderPosition = 0f
    private val colorBackground = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.LTGRAY }
    private val colorSlider = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.WHITE }
    private val colorOn = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.GREEN }
    private val colorOff = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = Color.GRAY }
    private var sliderRadius = 0f
    private var padding = 8f
    var onToggleChangedListener: ((Boolean) -> Unit)? = null
    private var downX = 0f
    private var isClick = false

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = 120
        val desiredHeight = 60
        val width = resolveSize(desiredWidth, widthMeasureSpec)
        val height = resolveSize(desiredHeight, heightMeasureSpec)

        setMeasuredDimension(width, height)
        sliderRadius = (height - padding * 2) / 2
        sliderPosition = if (isOn) width - padding - sliderRadius * 2 else padding
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()
        val rectangle = RectF(0f, 0f, width, height)
        val color = if (isOn) colorOn.color else colorOff.color
        colorBackground.color = color
        canvas.drawRoundRect(rectangle, height / 2, height / 2, colorBackground)

        val sliderLeft = sliderPosition
        val sliderRect = RectF(sliderLeft, padding, sliderLeft + sliderRadius * 2, height - padding)
        canvas.drawRoundRect(sliderRect, sliderRadius, sliderRadius, colorSlider)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.x
                isClick = true
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = event.x - downX
                if (Math.abs(dx) > 10) isClick = false
                sliderPosition += dx
                downX = event.x
                sliderPosition = sliderPosition.coerceIn(padding, width - padding - sliderRadius * 2)
                invalidate()
                return true
            }
            MotionEvent.ACTION_UP -> {
                val newState = if (isClick) {
                    event.x >= width / 2
                } else {
                    sliderPosition >= (width - sliderRadius * 2) / 2
                }

                animateSlider(newState)

                if (newState != isOn) {
                    isOn = newState
                    onToggleChangedListener?.invoke(isOn)
                }
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    fun setToggleState(isOn: Boolean) {
        if (this.isOn != isOn) {
            this.isOn = isOn
            sliderPosition = if (isOn) width - padding - sliderRadius * 2 else padding
            invalidate()
        }
    }

    private fun animateSlider(targetState: Boolean) {
        val start = sliderPosition
        val end = if (targetState) width - padding - sliderRadius * 2 else padding

        ValueAnimator.ofFloat(start, end).apply {
            duration = 200
            interpolator = FastOutSlowInInterpolator()
            addUpdateListener { animation -> sliderPosition = animation.animatedValue as Float
                invalidate()
            }
            start()
        }
    }
}
