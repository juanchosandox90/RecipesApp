package com.sandoval.recipesapp.data

import com.sandoval.recipesapp.data.local.LocalDataSource
import com.sandoval.recipesapp.data.remote.RemoteDataSource
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class Repository @Inject constructor(
    remoteDataSource: RemoteDataSource, localDataSource: LocalDataSource
) {
    val remote = remoteDataSource
    val local = localDataSource
}