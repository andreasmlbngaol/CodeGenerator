package com.andreasmlbngaol.codegenerator.model.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CodeDao {
    @Upsert suspend fun upsertCode(code: Code)

    @Delete suspend fun deleteCode(code: Code)

    @Query("SELECT * FROM code")
    fun getAll(): Flow<List<Code>>

    @Query("SELECT * FROM code WHERE id = :id")
    suspend fun getCodeById(id: Int): Code?

    @Query("SELECT * FROM code WHERE title LIKE '%' || :query || '%'")
    fun searchCode(query: String): Flow<List<Code>>

    @Query("SELECT * FROM code ORDER BY title ASC")
    fun getCodeSortedByTitle(): Flow<List<Code>>

    @Query("SELECT * FROM code ORDER BY createdAt ASC")
    fun getCodeSortedByDate(): Flow<List<Code>>

    @Query("SELECT * FROM code ORDER BY content ASC")
    fun getCodeSortedByContent(): Flow<List<Code>>


}