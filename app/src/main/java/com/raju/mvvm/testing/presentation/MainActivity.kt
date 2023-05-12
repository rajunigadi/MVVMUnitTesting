package com.raju.mvvm.testing.presentation

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.raju.mvvm.testing.databinding.ActivityMainBinding
import com.raju.mvvm.testing.utils.DispatcherProvider
import com.raju.mvvm.testing.utils.UiState
import com.raju.mvvm.testing.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<UserViewModel>()

    @Inject
    lateinit var dispatcherProvider: DispatcherProvider

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        showToast(exception.message ?: "Something went wrong")
    }

    private val userAdapter = UserAdapter {
        showToast("Clicked ${it.name}")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerView.adapter = userAdapter
        setupObserver()
        viewModel.getUsers()
    }

    private fun setupObserver() {
        lifecycleScope.launch(dispatcherProvider.main + exceptionHandler) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    when (it) {
                        is UiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            userAdapter.submitList(it.data)
                            binding.recyclerView.visibility = View.VISIBLE
                        }
                        is UiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.recyclerView.visibility = View.GONE
                        }
                        is UiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            showToast(it.message)
                        }
                    }
                }
            }
        }
    }
}