package com.raju.mvvm.testing

import com.raju.mvvm.testing.data.User
import com.raju.mvvm.testing.domain.UseCase
import com.raju.mvvm.testing.presentation.UserViewModel
import com.raju.mvvm.testing.utils.DispatcherProvider
import com.raju.mvvm.testing.utils.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class UserViewModelTest {

    @Mock
    lateinit var useCase: UseCase

    @Mock
    lateinit var dispatcherProvider: DispatcherProvider

    private lateinit var viewModel: UserViewModel

    @Before
    fun setup() {
        viewModel = UserViewModel(useCase, dispatcherProvider)
    }

    @Test
    fun `getUsers() should update uiState with success state when useCase returns data`() =
        runTest {
            // Given
            val expectedUsers = listOf(User("John"), User("Jane"))
            val flow = flow { emit(expectedUsers) }

            `when`(useCase.execute()).thenReturn(flow)
            `when`(dispatcherProvider.main).thenReturn(Dispatchers.Unconfined)
            `when`(dispatcherProvider.io).thenReturn(Dispatchers.Unconfined)

            // When
            viewModel.getUsers()

            // Then
            val actualUiState = viewModel.uiState.value
            assertEquals(UiState.Success(expectedUsers), actualUiState)
        }

    @Test
    fun `getUsers() should update uiState with error state when useCase throws an exception`() =
        runTest {

            // Given
            val expectedErrorMessage = "Something went wrong"
            val flow = flow<List<User>> { throw Exception(expectedErrorMessage) }

            `when`(useCase.execute()).thenReturn(flow)
            `when`(dispatcherProvider.main).thenReturn(Dispatchers.Unconfined)
            `when`(dispatcherProvider.io).thenReturn(Dispatchers.Unconfined)

            // When
            viewModel.getUsers()

            // Then
            val actualUiState = viewModel.uiState.value
            assertEquals(UiState.Error(expectedErrorMessage), actualUiState)
        }

    @Test
    fun `getUsers() should update uiState with loading state when called`() = runTest {
        // Given
        val flow = flow<List<User>> { emptyList<User>() }

        `when`(useCase.execute()).thenReturn(flow)
        `when`(dispatcherProvider.main).thenReturn(Dispatchers.Unconfined)
        `when`(dispatcherProvider.io).thenReturn(Dispatchers.Unconfined)

        // When
        viewModel.getUsers()

        // Then
        val actualUiState = viewModel.uiState.value
        assertEquals(UiState.Loading, actualUiState)
    }
}
