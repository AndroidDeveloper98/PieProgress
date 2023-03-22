package com.deepan.pieprogress

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.annotation.TargetApi
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.view.View
import kotlin.properties.Delegates

class PieProgressBar : View {

    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val tickPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rect: RectF by lazy {
        RectF(0f, 0f, width.toFloat() - strokePaint.strokeWidth, height.toFloat() - strokePaint.strokeWidth)
    }
    private var progress = 0f
    private var progressCompleteListener : ProgressCompleteListener? = null

    var isCompleted: Boolean by Delegates.observable(false) { _, _, newValue ->
        if (newValue) invalidate()
    }

    interface ProgressCompleteListener {
        fun progressCompleteListener()
    }

    fun setOnProgressCompleteListener(progressCompleteListener: ProgressCompleteListener){
        this.progressCompleteListener = progressCompleteListener
    }

    constructor(context: Context) : super(context) {
        init(context, null, -1, -1)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs, -1, -1)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs, defStyleAttr, -1)
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val attributes = context.theme.obtainStyledAttributes(attrs, R.styleable.PieProgress, defStyleAttr, defStyleRes)
        try {
            val progressColor = attributes.getColor(
                R.styleable.PieProgress_progressColor, context.resources.getColor(R.color.white, context.theme)
            )
            val tempColor = attributes.getColor(
                R.styleable.PieProgress_progressColor, context.resources.getColor(R.color.black, context.theme)
            )
            val strokeWidthAttribute = attributes.getDimension(
                R.styleable.PieProgress_strokeWidth, context.resources.getDimension(R.dimen.width_circle_stroke)
            )
            progressPaint.apply {
                color = progressColor
            }
            tickPaint.apply {
                color = progressColor
                style = Paint.Style.STROKE
                strokeCap = Paint.Cap.ROUND
                strokeWidth = strokeWidthAttribute
            }
            strokePaint.apply {
                color = tempColor
                style = Paint.Style.FILL
                strokeWidth = strokeWidthAttribute
            }
        } finally {
            attributes.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas?) {
       // canvas?.drawCircle(rect.centerX(), rect.centerY(), (width / 2 - strokePaint.strokeWidth), strokePaint)
        canvas?.drawCircle(rect.centerX(), rect.centerY(), (width / 2 - strokePaint.strokeWidth), strokePaint)
        canvas?.drawArc(rect, 360f, (progress * 3.6).toFloat(), true, progressPaint)
        super.onDraw(canvas)
    }

    fun setProgress(progress: Float) {
        this.progress = progress
        invalidate()
        if (progress.toInt() == 100) {
            isCompleted = true
            progressCompleteListener?.progressCompleteListener()
        }
    }
}