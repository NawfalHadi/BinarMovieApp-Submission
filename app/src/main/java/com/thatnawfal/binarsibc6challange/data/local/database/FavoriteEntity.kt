package com.thatnawfal.binarsibc6challange.data.local.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Dao
@Parcelize
@Entity(tableName = "favorite")
data class FavoriteEntity(
    @PrimaryKey
    var id: Int = 0,
    @ColumnInfo(name = "uid")
    var uid: String?,
    @ColumnInfo(name = "poster")
    var poster: String?,
) : Parcelable