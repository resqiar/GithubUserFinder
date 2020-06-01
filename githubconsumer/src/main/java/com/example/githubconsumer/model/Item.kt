package com.example.githubconsumer.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Item(
    var id: Int = 0,
    var login: String? = null,
    var avatar_url: String? = null,
    var following_count: Int? = 0,
    var followers_count: Int? = 0,
    var total_count: Int? = 0

    ): Parcelable