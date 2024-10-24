package io.github.kabirnayeem99.zakira.data.service.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import io.github.kabirnayeem99.zakira.data.dto.PhraseRoomEntity

@Dao
interface PhraseRoomDao {
    @Insert
    suspend fun insert(phraseRoomEntity: PhraseRoomEntity): Long

    @Query("SELECT * FROM phrases WHERE category_id IN (:categoryIds) ORDER BY arabic LIMIT 10 OFFSET :offset")
    suspend fun getAll(categoryIds: List<Long>, offset: Int): List<PhraseRoomEntity>

    @Query("SELECT * FROM phrases WHERE category_id IN (:categoryIds) AND is_favourite = 1 ORDER BY arabic LIMIT 10 OFFSET :offset")
    suspend fun getOnlyFavouritesAll(categoryIds: List<Long>, offset: Int): List<PhraseRoomEntity>


    @Query("SELECT * FROM phrases WHERE id=:phraseId")
    suspend fun getById(phraseId: Long): PhraseRoomEntity

    @Query("DELETE FROM phrases WHERE id=:phraseId")
    suspend fun deleteById(phraseId: Long)

    @Query("SELECT * FROM phrases WHERE category_id = :categoryId")
    suspend fun getPhrasesByCategory(categoryId: Int): List<PhraseRoomEntity>

    @Update
    suspend fun updatePhrase(phrase: PhraseRoomEntity)

    @Query("SELECT COUNT(*) FROM phrases WHERE arabic = :arabicText OR arabic_without_tashkeel = :arabicText OR meaning = :meaningText")
    suspend fun existsByArabicOrMeaning(arabicText: String, meaningText: String): Boolean

    @Query(
        """
    SELECT * FROM phrases
    
    WHERE (category_id IN (:categoryIds)) AND (arabic LIKE '%' || :searchString || '%' 
       OR arabic_without_tashkeel LIKE '%' || :searchString || '%'
       OR meaning LIKE '%' || :searchString || '%')
    ORDER BY arabic   
    LIMIT 30
    """
    )
    suspend fun searchPhrases(searchString: String, categoryIds: List<Long>): List<PhraseRoomEntity>

    @Query("UPDATE phrases SET is_favourite = NOT is_favourite, is_read = 1  WHERE id = :phraseId")
    suspend fun toggleFavourite(phraseId: Long)

    @Query("UPDATE phrases SET is_read = CASE WHEN is_read = 0 THEN 1 ELSE 0 END WHERE id = :phraseId")
    suspend fun toggleRead(phraseId: Long)
}