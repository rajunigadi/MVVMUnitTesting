package com.raju.mvvm.testing.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raju.mvvm.testing.data.User
import com.raju.mvvm.testing.domain.UseCase
import com.raju.mvvm.testing.utils.DispatcherProvider
import com.raju.mvvm.testing.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val useCase: UseCase,
    private val dispatcherProvider: DispatcherProvider
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<User>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<User>>> = _uiState

    fun getUsers() {
        viewModelScope.launch(dispatcherProvider.main) {
            _uiState.value = UiState.Loading
            useCase
                .execute()
                .flowOn(dispatcherProvider.io)
                .catch { e ->
                    _uiState.value = UiState.Error(e.message ?: "Something went wrong")
                }
                .collect { users ->
                    _uiState.value = UiState.Success(users)
                }
        }
    }
}