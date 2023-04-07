package com.sandoval.recipesapp.data.remote

import com.sandoval.recipesapp.data.api.RecipesApi
import com.sandoval.recipesapp.data.models.FoodJoke
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val recipesApi: RecipesApi
) {
    suspend fun getRecipes(queries: Map<String, String>) =
        recipesApi.getRecipes(queries)

    suspend fun searchRecipes(queries: Map<String, String>) =
        recipesApi.searchRecipes(queries)

    suspend fun getFoodJoke(apiKey: String): Response<FoodJoke> {
        return recipesApi.getFoodJoke(apiKey)
    }
}