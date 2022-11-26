package com.dhabasoft.androidintermediate.utils.customview

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat
import com.dhabasoft.androidintermediate.R

/**
 * Created by dhaba
 */
fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

fun getIconUnifyDrawable(
    context: Context,
    iconId: Int,
    assetColor: Int? = null
): Drawable? {
    return try {
        val img = AppCompatResources.getDrawable(context, iconId)
        val color =
                assetColor ?: ContextCompat.getColor(context, R.color.icon_enable_default_color)
        img?.colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(color, BlendModeCompat.SRC_ATOP)
        img?.mutate()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()