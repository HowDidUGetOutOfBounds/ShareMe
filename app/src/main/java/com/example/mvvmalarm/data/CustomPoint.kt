package com.example.mvvmalarm.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [
        Index(
            value = ["message"],
            unique = true
        )
    ]
)
data class CustomPoint(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var lat: Double = 0.0,
    var lon: Double = 0.0,
    var imgUri: String? = null,
    var date: String? = null,
    @ColumnInfo(name = "message")
    var message: String? = null
)