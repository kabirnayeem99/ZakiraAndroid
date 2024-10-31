package io.github.kabirnayeem99.zakira.data.service.db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.github.kabirnayeem99.zakira.data.dto.CategoryRoomEntity
import io.github.kabirnayeem99.zakira.data.dto.PhraseRoomEntity

@Database(entities = [CategoryRoomEntity::class, PhraseRoomEntity::class], version = 1)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryRoomDao
    abstract fun phraseDao(): PhraseRoomDao

    companion object {
        @Volatile
        private var INSTANCE: LocalDatabase? = null
        fun createLocalDatabase(context: Context): LocalDatabase {
            try {
                val instance = Room.databaseBuilder(
                    context, LocalDatabase::class.java, "zakira_local_database"
                ).build()
                INSTANCE = instance
                return instance
            } catch (e: Exception) {
                Log.e(TAG, "createLocalDatabase: Failed, because, ${e.message}", e)
                return INSTANCE!!
            }
        }
    }
}

private const val TAG = "LocalDatabase"
