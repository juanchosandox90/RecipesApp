package com.sandoval.recipesapp.viewmodel

import android.app.Application
import com.sandoval.recipesapp.data.local.DataStoreRepository
import com.sandoval.recipesapp.ui.list_recipes.viewmodels.RecipesViewModel
import com.sandoval.recipesapp.utils.Constants.Companion.API_KEY
import com.sandoval.recipesapp.utils.Constants.Companion.DEFAULT_DIET_TYPE
import com.sandoval.recipesapp.utils.Constants.Companion.DEFAULT_MEAL_TYPE
import com.sandoval.recipesapp.utils.Constants.Companion.DEFAULT_RECIPES_NUMBER
import com.sandoval.recipesapp.utils.Constants.Companion.QUERY_ADD_RECIPE_INFORMATION
import com.sandoval.recipesapp.utils.Constants.Companion.QUERY_API_KEY
import com.sandoval.recipesapp.utils.Constants.Companion.QUERY_DIET
import com.sandoval.recipesapp.utils.Constants.Companion.QUERY_FILL_INGREDIENTS
import com.sandoval.recipesapp.utils.Constants.Companion.QUERY_NUMBER
import com.sandoval.recipesapp.utils.Constants.Companion.QUERY_SEARCH
import com.sandoval.recipesapp.utils.Constants.Companion.QUERY_TYPE
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RecipesViewModelTest {

    private lateinit var dataStoreRepository: DataStoreRepository
    private lateinit var application: Application
    private lateinit var viewModel: RecipesViewModel
    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        dataStoreRepository = mockk()
        application = mockk()

        every { dataStoreRepository.readMealAndDietType } returns null
        every { dataStoreRepository.readBackOnline } returns null
        coEvery { dataStoreRepository.saveBackOnline(any()) } returns Unit
        coEvery {
            dataStoreRepository.saveMealAndDietType(
                any(), any(), any(), any()
            )
        } returns Unit

        viewModel = RecipesViewModel(dataStoreRepository, application)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
        testScope.cleanupTestCoroutines()
    }

    @Test
    fun `test applyQueries Method`() = testScope.runTest {
        val queries = viewModel.applyQueries()
        assertNotNull(queries)
        assertEquals(queries[QUERY_NUMBER], DEFAULT_RECIPES_NUMBER)
        assertEquals(queries[QUERY_API_KEY], API_KEY)
        assertEquals(queries[QUERY_TYPE], DEFAULT_MEAL_TYPE)
        assertEquals(queries[QUERY_DIET], DEFAULT_DIET_TYPE)
        assertEquals(queries[QUERY_ADD_RECIPE_INFORMATION], "true")
        assertEquals(queries[QUERY_FILL_INGREDIENTS], "true")
    }

    @Test
    fun `test applySearch Query Method`() = testScope.runTest {
        val searchQuery = "Chicken"
        val queries = viewModel.applySearchQuery(searchQuery)
        assertNotNull(queries)
        assertEquals(queries[QUERY_SEARCH], searchQuery)
        assertEquals(queries[QUERY_NUMBER], DEFAULT_RECIPES_NUMBER)
        assertEquals(queries[QUERY_API_KEY], API_KEY)
        assertEquals(queries[QUERY_ADD_RECIPE_INFORMATION], "true")
        assertEquals(queries[QUERY_FILL_INGREDIENTS], "true")
    }

}