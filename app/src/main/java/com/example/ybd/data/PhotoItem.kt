package com.example.ybd.data

import androidx.annotation.DrawableRes

data class PhotoItem(
    @DrawableRes val photoResId: Int?, // Nullable, если только подпись
    val caption: String
)