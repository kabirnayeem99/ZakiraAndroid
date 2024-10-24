package io.github.kabirnayeem99.zakira.data.service.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import io.github.kabirnayeem99.zakira.data.dto.CategoryRoomEntity

@Dao
interface CategoryRoomDao {
    @Insert
    suspend fun insert(category: CategoryRoomEntity): Long

    @Query("SELECT * FROM categories")
    suspend fun getAllCategories(): List<CategoryRoomEntity>
}