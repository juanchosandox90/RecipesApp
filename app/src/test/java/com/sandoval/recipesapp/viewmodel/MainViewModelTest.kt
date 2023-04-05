package com.sandoval.recipesapp.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import com.sandoval.recipesapp.data.Repository
import com.sandoval.recipesapp.ui.list_recipes.viewmodels.MainViewModel
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {


    @Mock
    private lateinit var repository: Repository
    private lateinit var context: Application
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        context = mockk()
        connectivityManager = mockk()
        viewModel = MainViewModel(repository, context)
        every { context.getSystemService(Context.CONNECTIVITY_SERVICE) } returns connectivityManager
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