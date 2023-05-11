package com.raju.mvvm.testing.domain

import com.raju.mvvm.testing.data.User
import com.raju.mvvm.testing.utils.DispatcherProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class UserUseCase(
    private val repository: UserRepository,
    private val dispatcherProvider: DispatcherProvider
) : UseCase {

    override suspend fun execute(): Flow<List<User>> {
        return flow { emit(repository.getUsers()) }.flowOn(dispatcherProvider.io)
    }
}