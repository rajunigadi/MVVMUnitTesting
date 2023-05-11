package com.raju.mvvm.testing

import com.raju.mvvm.testing.data.User
import com.raju.mvvm.testing.data.UserApi
import com.raju.mvvm.testing.data.UserRepositoryImpl
import com.raju.mvvm.testing.domain.UserRepository
import kotlinx.coroutines.test.*
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UserRepositoryTest {

    @Mock
    lateinit var userApi: UserApi

    private lateinit var repository: UserRepository

    @Before
    fun setup() {
        repository = UserRepositoryImpl(userApi)
    }

    @Test
    fun `getUsers() should return list of users when API call is successful`() =
        runTest {
            // Given
            val expectedUsers = listOf(User("John"), User("Jane"))

            `when`(userApi.getUsers()).thenReturn(expectedUsers)

            // When
            repository.getUsers()

            // Then
            val actualUsers = repository.getUsers()
            Assertions.assertEquals(expectedUsers, actualUsers)
        }

    @Test(expected = Exception::class)
    fun `getUsers() should throw an exception when API call fails`() = runTest {
        // Given
        val expected = Exception("Something went wrong")

        `when`(userApi.getUsers()).thenThrow(expected)

        // When
        repository.getUsers()

        // Then
        val actual = repository.getUsers()
        Assertions.assertEquals(expected, actual)
    }
}