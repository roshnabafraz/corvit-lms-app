package com.corvit.corvit_lms.screens.components

import android.annotation.SuppressLint
import android.content.Context
import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.corvit.corvit_lms.R

@SuppressLint("LocalContextResourcesRead", "DiscouragedApi")
@DrawableRes
@Composable
fun getCategoryImageResId(order: Int): Int {
    val context: Context = LocalContext.current
    val resourceName = "img_$order"

    val resId = context.resources.getIdentifier(
        resourceName,
        "drawable",
        context.packageName
    )

    return if (resId != 0) resId else R.drawable.demo
}