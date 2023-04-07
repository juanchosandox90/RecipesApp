package com.sandoval.recipesapp.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import com.sandoval.recipesapp.data.Repository
import com.sandoval.recipesapp.data.local.LocalDataSource
import com.sandoval.recipesapp.data.models.FoodRecipe
import com.sandoval.recipesapp.data.models.Result
import com.sandoval.recipesapp.ui.list_recipes.viewmodels.MainViewModel
import com.sandoval.recipesapp.utils.NetworkResult
import io.mockk.*
import junit.framework.TestCase.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    private lateinit var repository: Repository
    private lateinit var context: Application
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var localDataSource: LocalDataSource
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        context = mockk()
        connectivityManager = mockk()
        repository = mockk()
        localDataSource = mockk()
        every { repository.local } returns localDataSource
        every { localDataSource.readDataBase() } returns flowOf(listOf())
        coEvery { localDataSource.insertRecipes(any()) } just Runs
        every { localDataSource.readFoodJoke() } returns flowOf(listOf())
        coEvery { localDataSource.insertFoodJoke(any()) } just Runs
        viewModel = MainViewModel(repository, context)
        every { context.getSystemService(Context.CONNECTIVITY_SERVICE) } returns connectivityManager
    }

    @Test
    fun `test handleRecipesResponse method with successful response`() {
        val mockedResponse = Response.success(
            FoodRecipe(
                results = listOf(
                    Result(
                        aggregateLikes = 234,
                        cheap = false,
                        dairyFree = true,
                        extendedIngredients = emptyList(),
                        glutenFree = true,
                        id = 1,
                        image = "imageUrl",
                        readyInMinutes = 200,
                        sourceName = "sajsa",
                        sourceUrl = "aisjas",
                        summary = "asuha",
                        title = "summary",
                        vegan = true,
                        vegetarian = true,
                        veryHealthy = true
                    )
                )
            )
        )

        val result = viewModel.handleRecipesResponse(mockedResponse)

        assertTrue(result is NetworkResult.Success)
        assertEquals(
            mockedResponse.body(), (result as NetworkResult.Success).data
        )
    }


    @Test
    fun `test handleRecipesResponse method with API key limited error`() {
        val mockedResponse = Response.error<FoodRecipe>(
            402, ResponseBody.create(
                MediaType.parse("application/json"), "API key limited error"
            )
        )

        val result = viewModel.handleRecipesResponse(mockedResponse)

        assertTrue(result is NetworkResult.Error)
        assertEquals(
            "API Key Limited.", (result as NetworkResult.Error).message
        )
    }

    @Test
    fun `test handleRecipesResponse method with empty results error`() {
        val mockedResponse = Response.success(FoodRecipe(results = emptyList()))

        val result = viewModel.handleRecipesResponse(mockedResponse)

        assertTrue(result is NetworkResult.Error)
        assertEquals(
            "Recipes Not Found.", (result as NetworkResult.Error).message
        )
    }

    @Test
    fun `test hasInternetConnection when network is available`() {
        every { connectivityManager.activeNetwork } returns mockk<Network>()
        every { connectivityManager.getNetworkCapabilities(any()) } returns mockk<NetworkCapabilities>().apply {
            every { hasTransport(NetworkCapabilities.TRANSPORT_WIFI) } returns true
            every { hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) } returns true
            every { hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) } returns true
        }

        val result = viewModel.hasInternetConnection()

        assertTrue(result)
    }

    @Test
    fun `test hasInternetConnection when network is not available`() {
        every { connectivityManager.activeNetwork } returns null
        every { connectivityManager.getNetworkCapabilities(null) } returns null

        val result = viewModel.hasInternetConnection()

        assertFalse(result)
    }

}