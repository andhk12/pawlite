package com.example.pawlite

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class JournalEntry(
    val date: String,
    val title: String,
    val description: String,
    // Ubah tipe data dari Int menjadi String
    val imageUrl: String
) : Parcelable