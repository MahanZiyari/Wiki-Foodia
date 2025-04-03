package ir.mahan.wikifoodia.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ir.mahan.wikifoodia.data.database.entity.DetailEntity
import ir.mahan.wikifoodia.data.database.entity.RecipeEntity

@Database(entities = [RecipeEntity::class, DetailEntity::class], version = 2, exportSchema = false)
@TypeConverters(CustomTypeConverter::class)
abstract class RecipeAppDatabase: RoomDatabase() {
    abstract fun dao(): RecipeAppDao
}