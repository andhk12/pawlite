package com.example.pawlite

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ResourceEntry(
    val tag: String,
    val title: String,
    val date: String,
    val description: String,
    // Ubah tipe data dari Int menjadi String
    val imageUrl: String
) : Parcelable