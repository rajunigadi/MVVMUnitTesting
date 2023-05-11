package com.raju.mvvm.testing.domain

import com.raju.mvvm.testing.data.User
import kotlinx.coroutines.flow.Flow

interface UseCase {
    suspend fun execute(): Flow<List<User>>
}

/*abstract class UseCase<in Param, out Result> {

    protected abstract suspend fun execute(param: Param? = null): Result

    open suspend operator fun invoke(param: Param? = null): Result {
        return try {
            execute(param)
        } catch (e: Exception) {
            throw e
        }
    }
}*/
