package com.raju.mvvm.testing

import com.raju.mvvm.testing.data.User
import com.raju.mvvm.testing.domain.UserRepository
import com.raju.mvvm.testing.domain.UserUseCase
import com.raju.mvvm.testing.utils.DispatcherProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UserUseCaseTest {

    @Mock
    lateinit var repository: UserRepository

    @Mock
    lateinit var dispatcherProvider: DispatcherProvider

    private lateinit var userUseCase: UserUseCase

    @Before
    fun setup() {
        userUseCase = UserUseCase(repository = repository, dispatcherProvider = dispatcherProvider)
    }

    @Test
    fun `execute should return flow of users`() = runTest {
        // Given
        val expectedUsers = listOf(User("John"), User("Jane"))

        `when`(repository.getUsers()).thenReturn(expectedUsers)
        `when`(dispatcherProvider.io).thenReturn(Dispatchers.Unconfined)

        // When
        val flow = userUseCase.execute()

        // Then
        flow.collect {
            assertEquals(expectedUsers, it)
        }
    }

    @Test
    fun `execute should return empty list on error`() = runTest {
        // Given
        `when`(repository.getUsers()).thenThrow(RuntimeException())
        `when`(dispatcherProvider.io).thenReturn(Dispatchers.Unconfined)

        // When
        val flow = userUseCase.execute()

        // Then
        flow.catch { assertTrue(it is RuntimeException) }.collect {
            assertEquals(emptyList<User>(), it)
        }
    }
}