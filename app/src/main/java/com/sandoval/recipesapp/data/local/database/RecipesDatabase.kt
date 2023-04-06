package com.sandoval.recipesapp.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sandoval.recipesapp.data.local.RecipesTypeConverters
import com.sandoval.recipesapp.data.local.dao.RecipesDao
import com.sandoval.recipesapp.data.local.entity.FoodJokeEntity
import com.sandoval.recipesapp.data.local.entity.RecipesEntity

@Database(
    entities = [RecipesEntity::class, FoodJokeEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(RecipesTypeConverters::class)
abstract class RecipesDatabase : RoomDatabase() {

    abstract fun recipesDao(): RecipesDao

}