package com.sandoval.recipesapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sandoval.recipesapp.data.models.FoodRecipe
import com.sandoval.recipesapp.utils.Constants.Companion.RECIPES_TABLE

@Entity(tableName = RECIPES_TABLE)
class RecipesEntity(
    var foodRecipe: FoodRecipe
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}