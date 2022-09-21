package com.example.notesapp.Dao

import androidx.room.*
import com.example.notesapp.entities.Notes

@Dao
interface NoteDao {
    @Query ("SELECT * FROM notes order by id DESC")
    suspend fun getAllNotes (): List<Notes>


    @Query ("SELECT * FROM notes WHERE id = :id ")
    suspend fun getSpecificNote (id : Int): Notes

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotes(note : Notes)

    @Delete
    suspend fun deleteNote(note : Notes)

    @Update
    suspend fun updateNote(note : Notes)

    @Query("DELETE FROM notes WHERE id =:id")
    suspend fun deleteSpecificNote(id:Int)

}