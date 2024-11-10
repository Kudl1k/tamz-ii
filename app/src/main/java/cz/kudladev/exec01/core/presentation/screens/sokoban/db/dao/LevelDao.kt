package cz.kudladev.exec01.core.presentation.screens.sokoban.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import cz.kudladev.exec01.core.presentation.screens.sokoban.db.entity.LevelDTO

@Dao
interface LevelDao {

    @Query("SELECT * FROM levels")
    fun getLevels(): List<LevelDTO>

    @Query("SELECT * FROM levels WHERE id = :id")
    fun getLevel(id: Int): LevelDTO

    @Upsert
    fun insertLevel(levelDTO: LevelDTO)

    @Upsert
    fun insertLevels(levels: List<LevelDTO>)

    @Update
    fun updateLevel(levelDTO: LevelDTO)

    @Delete
    fun deleteLevel(levelDTO: LevelDTO)
}