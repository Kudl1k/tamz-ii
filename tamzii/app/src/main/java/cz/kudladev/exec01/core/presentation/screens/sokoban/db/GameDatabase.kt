package cz.kudladev.exec01.core.presentation.screens.sokoban.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import cz.kudladev.exec01.core.presentation.screens.sokoban.db.converters.Converters
import cz.kudladev.exec01.core.presentation.screens.sokoban.db.dao.LevelDao
import cz.kudladev.exec01.core.presentation.screens.sokoban.db.entity.LevelDTO

@Database(
    entities = [LevelDTO::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class GameDatabase : RoomDatabase() {
    abstract fun levelDao(): LevelDao
}