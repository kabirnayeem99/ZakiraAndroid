package io.github.kabirnayeem99.zakira.data.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "phrases", foreignKeys = [ForeignKey(
        entity = CategoryRoomEntity::class,
        parentColumns = ["id"],
        childColumns = ["category_id"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.NO_ACTION
    )]
)
data class PhraseRoomEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") val id: Long = 0,

    @ColumnInfo(name = "category_id", index = true) val categoryId: Long,

    @ColumnInfo(name = "arabic") val arabic: String,

    @ColumnInfo(name = "arabic_without_tashkeel") val arabicWithoutTashkeel: String,

    @ColumnInfo(name = "meaning") val meaning: String,

    @ColumnInfo(name = "is_favourite", defaultValue = "0") val isFavourite: Boolean = false,

    @ColumnInfo(name = "is_read", defaultValue = "0") val isRead: Int = 0,

    @ColumnInfo(name = "is_custom", defaultValue = "0") val isCustom: Int = 0,
)