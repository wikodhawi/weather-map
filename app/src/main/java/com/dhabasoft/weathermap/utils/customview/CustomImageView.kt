package com.dhabasoft.weathermap.utils.customview

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.dhabasoft.weathermap.R
import java.io.File
import java.net.URI
import java.util.*

class CustomImageView : AppCompatImageView, LifecycleObserver {

    private var cornerRadius: Int = 8
    private var heightRatio: Float? = null
    private var urlSrc: String = ""
        set(value) {
            field = value

            if (urlSrc.isNotEmpty()) {
                setImageUrl(value)
            }
        }

    var onUrlLoaded: ((isSuccess: Boolean) -> Any)? = null

    private var customLoadingAVD: AnimatedVectorDrawableCompat? = null
    private var disableShimmeringPlaceholder: Boolean = false
    private var shimmeringPlaceholder: AnimatedVectorDrawableCompat? = null
    private var placeholder: Int = 0

    private lateinit var rectF: RectF
    private val path = Path()
    private var paint = Paint()
    private var isMeasured = false
    private var isImageLoaded = false
    private var isLoadError = false
    private var hasImageUrl = false
    private var scaleTypeConfigOnDraw = false
    private var userPrefScaleType: ScaleType? = ScaleType.FIT_XY
    private var errorDrawable: LayerDrawable? = null
    private var isErrorDrawableNeedReInit = true
    private var shimmerDrawable: AnimatedVectorDrawableCompat? = null
    private var prevWidth = 0
    private var isAttached = false
    private var url: String = ""
    private var placeholderHeight: Int? = null
    private var isSkipCache: Boolean = false

    private val reloadIcon = getIconUnifyDrawable(
        context,
        CustomIcon.RELOAD,
        ContextCompat.getColor(context, R.color.Unify_N0)
    )
    private var reloadDrawable: LayerDrawable? = null

    private var defaultPlaceholderDrawable: LayerDrawable? = null

    private val defaultBackgroundDrawable =
        ColorDrawable(ContextCompat.getColor(context, R.color.Unify_N75))

    private var isRetryable = false

    constructor(context: Context) : super(context) {
        initPlaceholder()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initWithAttr(context, attributeSet)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        initWithAttr(context, attributeSet)
    }

    init {
        paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ContextCompat.getColor(context, R.color.Neutral_N0)
            xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        }

        try {
            reloadDrawable = LayerDrawable(
                arrayOf(
                    AppCompatResources.getDrawable(
                        context,
                        R.drawable.ic_imagestate_reload
                    ), reloadIcon
                )
            )
        } catch (e: Exception) {
        }

        try {
            defaultPlaceholderDrawable = LayerDrawable(
                arrayOf(
                    AppCompatResources.getDrawable(
                        context,
                        R.drawable.iconunify_error
                    )
                )
            )
        } catch (e: Exception) {
        }

        registerLifecycle()
    }

    private fun initWithAttr(context: Context, attributeSet: AttributeSet) {
        val attributeArray = context.obtainStyledAttributes(attributeSet, R.styleable.UnifyImage)
        val androidAttributeArray = intArrayOf(
            android.R.attr.scaleType
        )
        val scaleType =
            context.obtainStyledAttributes(attributeSet, androidAttributeArray).getInt(0, -1)
        if (scaleType != -1) {
            userPrefScaleType = ScaleType.values()[scaleType]
        }

        initPlaceholder()
        urlSrc = attributeArray.getString(R.styleable.UnifyImage_unify_image_url_src) ?: ""
        attributeArray.recycle()
    }

    private fun initPlaceholder() {
        if (placeholder == 0 && drawable == null) {
            setImageDrawable(defaultPlaceholderDrawable)
            background = defaultBackgroundDrawable
        } else {
            setBackgroundResource(placeholder)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        rectF = RectF(0f, 0f, w.toFloat(), h.toFloat())
        resetPath()

        // reload dimension is 32dp for small image (< 256dp)
        var reloadPaddingH = measuredWidth / 2 - 16.toPx()
        var reloadIconPaddingH = measuredWidth / 2 - 12.toPx()
        var reloadPaddingV = measuredHeight / 2 - 16.toPx()
        var reloadIconPaddingV = measuredHeight / 2 - 12.toPx()

        if (measuredWidth.toDp() > 256 && measuredHeight.toDp() > 256) {
            // reload dimension is 48dp for large image (< 256dp)
            reloadPaddingH = measuredWidth / 2 - 24.toPx()
            reloadIconPaddingH = measuredWidth / 2 - 16.toPx()
            reloadPaddingV = measuredHeight / 2 - 24.toPx()
            reloadIconPaddingV = measuredHeight / 2 - 16.toPx()

            errorDrawable?.setLayerInset(
                0,
                measuredWidth / 2 - 128.toPx(),
                measuredHeight / 2 - 128.toPx(),
                measuredWidth / 2 - 128.toPx(),
                measuredHeight / 2 - 128.toPx()
            )

            defaultPlaceholderDrawable?.setLayerInset(
                0,
                measuredWidth / 2 - 128.toPx(),
                measuredHeight / 2 - 128.toPx(),
                measuredWidth / 2 - 128.toPx(),
                measuredHeight / 2 - 128.toPx()
            )
        } else if (measuredWidth.toDp() <= 256 || measuredHeight.toDp() <= 256) {
            if (measuredWidth <= measuredHeight) {
                errorDrawable?.setLayerInset(
                    0,
                    0,
                    measuredHeight / 2 - measuredWidth / 2,
                    0,
                    measuredHeight / 2 - measuredWidth / 2
                )
                defaultPlaceholderDrawable?.setLayerInset(
                    0,
                    0,
                    measuredHeight / 2 - measuredWidth / 2,
                    0,
                    measuredHeight / 2 - measuredWidth / 2
                )
            } else {
                errorDrawable?.setLayerInset(
                    0,
                    measuredWidth / 2 - measuredHeight / 2,
                    0,
                    measuredWidth / 2 - measuredHeight / 2,
                    0
                )
                defaultPlaceholderDrawable?.setLayerInset(
                    0,
                    measuredWidth / 2 - measuredHeight / 2,
                    0,
                    measuredWidth / 2 - measuredHeight / 2,
                    0
                )
            }
        }

        reloadDrawable?.setLayerInset(
            0,
            reloadPaddingH,
            reloadPaddingV,
            reloadPaddingH,
            reloadPaddingV
        )
        reloadDrawable?.setLayerInset(
            1,
            reloadIconPaddingH,
            reloadIconPaddingV,
            reloadIconPaddingH,
            reloadIconPaddingV
        )
    }

    override fun draw(canvas: Canvas) {
        val save = when {
            /**
             * saveLayer without flag was added in API 21
             */
            Build.VERSION.SDK_INT >= 21 -> canvas.saveLayer(
                RectF(0f, 0f, width.toFloat(), height.toFloat()),
                null
            )
            /**
             * saveLayer with flag is deprecated in API 26
             */
            else -> canvas.saveLayer(
                RectF(0f, 0f, width.toFloat(), height.toFloat()),
                null,
                Canvas.ALL_SAVE_FLAG
            )
        }
        if (Build.VERSION.SDK_INT > 27) canvas.clipPath(path)
        super.draw(canvas)
        if (Build.VERSION.SDK_INT < 28) canvas.drawPath(path, paint)
        canvas.restoreToCount(save)
    }

    private fun resetPath() {
        path.reset()
        path.addRoundRect(
            rectF,
            cornerRadius.toPx().toFloat(),
            cornerRadius.toPx().toFloat(),
            Path.Direction.CW
        )
        path.close()
    }

    private fun setImageUrl(
        url: String,
        heightRatio: Float? = null,
        placeholderHeight: Int? = null,
        isSkipCache: Boolean = false
    ) {
        this.url = url
        this.placeholderHeight = placeholderHeight
        this.isSkipCache = isSkipCache
        this.heightRatio = heightRatio
        this.post {
            heightRatio?.let {
                configHeightRatio(it)
            }

            startShimmer()
            hasImageUrl = true

            if (placeholderHeight != null) {
                this.layoutParams.height = placeholderHeight
            }

            val ext = extReader(url)
            loadImage(url, placeholderHeight, isSkipCache)
        }
    }

    private fun applyLoopingAnimatedVectorDrawable() {
        shimmeringPlaceholder?.registerAnimationCallback(object :
            Animatable2Compat.AnimationCallback() {
            val mainHandler = Handler(Looper.getMainLooper())
            override fun onAnimationEnd(drawable: Drawable?) {
                mainHandler.post {
                    shimmeringPlaceholder!!.start()
                }
            }
        })
        shimmeringPlaceholder?.start()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        if (drawable == defaultPlaceholderDrawable) {
            scaleTypeConfigOnDraw = true
            scaleType = ScaleType.FIT_XY
            scaleTypeConfigOnDraw = false
        }

        if (drawable != null && drawable != defaultPlaceholderDrawable && drawable != errorDrawable && drawable != reloadDrawable) {
            this.background = null
            this.scaleType = userPrefScaleType
        }

        if (measuredWidth != prevWidth && heightRatio != null) {
            configHeightRatio(heightRatio!!)
            prevWidth = measuredWidth
        }
    }

    private fun onError() {
        onUrlLoaded?.invoke(false)
        this@CustomImageView.isImageLoaded = true
        this@CustomImageView.isLoadError = true
        this.background = defaultBackgroundDrawable
    }

    private fun loadImage(url: String, placeholderHeight: Int?, isSkipCache: Boolean) {
        if (!isAttached || !isValidContextForGlide(context)) return
        val mRequestBuilder: RequestBuilder<Bitmap> = Glide.with(context).asBitmap()

        if (heightRatio == null) mRequestBuilder.dontTransform()

        mRequestBuilder.load(url).error(
            if (isRetryable) reloadDrawable else {
                /**
                 * check if errorDrawable is already initialize / not
                 * if initialize but using temporary drawable then try to re-init errorDrawable on next set
                 */
                if (errorDrawable == null || isErrorDrawableNeedReInit) {
                    errorDrawable = try {
                        isErrorDrawableNeedReInit = false
                        LayerDrawable(
                            arrayOf(
                                AppCompatResources.getDrawable(
                                    context,
                                    R.drawable.iconunify_error
                                )
                            )
                        )
                    } catch (e: Exception) {
                        isErrorDrawableNeedReInit = true
                        val tempDrawable =
                            ColorDrawable(ContextCompat.getColor(context, R.color.Unify_NN50))
                        LayerDrawable(arrayOf(tempDrawable))
                    }
                }
                errorDrawable
            }
        ).listener(object : RequestListener<Bitmap> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Bitmap>?,
                isFirstResource: Boolean
            ): Boolean {
                clearShimmerAnimation()

                onError()
                return false
            }

            override fun onResourceReady(
                resource: Bitmap?,
                model: Any?,
                target: Target<Bitmap>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                clearShimmerAnimation()

                onUrlLoaded?.invoke(true)
                this@CustomImageView.isImageLoaded = true
                this@CustomImageView.scaleType = userPrefScaleType

                if (resource != null) {
                    renderSource(resource.width, resource.height, placeholderHeight)
                }
                return false
            }
        })
            .skipMemoryCache(isSkipCache)
            .diskCacheStrategy(if (isSkipCache) DiskCacheStrategy.NONE else DiskCacheStrategy.AUTOMATIC)
            .into(this)
    }

    private fun renderSource(w: Int, h: Int, placeholderHeight: Int?) {
        val hRatio: Float =
            (h.toDouble() / w.toDouble()).toFloat()
        if (this@CustomImageView.layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT || placeholderHeight != null) {
            this@CustomImageView.post {
                this@CustomImageView.layoutParams.height =
                    (this@CustomImageView.measuredWidth * hRatio).toInt()
                this@CustomImageView.requestLayout()
            }
        }
    }

    private fun clearShimmerAnimation() {
        background = null
        shimmeringPlaceholder?.stop()
        shimmeringPlaceholder?.clearAnimationCallbacks()
    }

    private fun extReader(url: String): String {
        val uri: URI? = try {
            URI(url)
        } catch (e: Exception) {
            null
        }

        return try {
            File(uri?.path).extension.lowercase(Locale.ENGLISH)
        } catch (e: Exception) {
            ""
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        isAttached = true

        if (url.isNotEmpty()) {
            setImageUrl(url, heightRatio, placeholderHeight, isSkipCache)
        }
    }

    override fun onDetachedFromWindow() {
        clearShimmerAnimation()

        super.onDetachedFromWindow()

        isAttached = false
    }

    override fun setScaleType(scaleType: ScaleType?) {
        super.setScaleType(scaleType)

        if (!scaleTypeConfigOnDraw) {
            userPrefScaleType = scaleType
        }
    }

    private fun configHeightRatio(h: Float) {
        heightRatio = h
        layoutParams.height = (measuredWidth * h).toInt()
        requestLayout()
        isMeasured = true
    }

    private fun startShimmer() {
        shimmerDrawable = shimmerDrawable ?: AnimatedVectorDrawableCompat.create(
            context,
            R.drawable.unify_loader_shimmer
        )

        shimmeringPlaceholder = customLoadingAVD ?: shimmerDrawable

        if (customLoadingAVD != null || !disableShimmeringPlaceholder) {
            background = shimmeringPlaceholder
            applyLoopingAnimatedVectorDrawable()
        } else {
            background = defaultBackgroundDrawable
        }

    }

    private fun isValidContextForGlide(paramContext: Context): Boolean {
        if (paramContext is Activity) {
            val paramActivity = context as Activity
            if (paramActivity.isFinishing || paramActivity.isDestroyed) {
                return false
            }
        }
        return true
    }

    private fun registerLifecycle() {
        try {
            val lifecycle = (context as? LifecycleOwner)?.lifecycle
            lifecycle?.addObserver(this)
        } catch (exception: Exception) {
            Log.d("Unify Image", "Failed to setup observer")
        }
    }
}