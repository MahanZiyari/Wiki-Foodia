package ir.mahan.wikifoodia.utils.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ir.mahan.wikifoodia.data.database.RecipeAppDatabase
import ir.mahan.wikifoodia.utils.constants.Constants
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(
        context,
        RecipeAppDatabase::class.java,
        Constants.DATABASE_NAME
    ).allowMainThreadQueries()
        .fallbackToDestructiveMigrationFrom()
        .build()


    @Provides
    @Singleton
    fun provideDao(database: RecipeAppDatabase) = database.dao()
}