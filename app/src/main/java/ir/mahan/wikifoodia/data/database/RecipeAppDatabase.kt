package ir.mahan.wikifoodia.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [RecipeEntity::class], version = 1, exportSchema = false)
@TypeConverters(CustomTypeConverter::class)
abstract class RecipeAppDatabase: RoomDatabase() {
    abstract fun dao(): RecipeAppDao
}