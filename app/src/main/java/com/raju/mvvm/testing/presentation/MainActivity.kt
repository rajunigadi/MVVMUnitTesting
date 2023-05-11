package com.raju.mvvm.testing.presentation

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.raju.mvvm.testing.databinding.ActivityMainBinding
import com.raju.mvvm.testing.pools.DefaultExecutorSupplier
import com.raju.mvvm.testing.pools.Priority
import com.raju.mvvm.testing.pools.PriorityRunnable
import com.raju.mvvm.testing.utils.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModels<UserViewModel>()

    private val userAdapter = UserAdapter {
        Toast.makeText(
            this@MainActivity,
            "Clicked ${it.name}",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerView.adapter = userAdapter
        setupObserver()
        viewModel.getUsers()

        doSomeMainThreadWork()
        doSomeMainThreadWork()
        doSomeLightWeightBackgroundWork()
    }

    private fun setupObserver() {
        lifecycleScope.launch {
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
                            Toast.makeText(
                                this@MainActivity,
                                it.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    /*
    * Using it for Background Tasks
    */
    fun doSomeBackgroundWork() {
        DefaultExecutorSupplier.getInstance().forBackgroundTasks()
            .submit(PriorityRunnable(Priority.IMMEDIATE)) {
                Log.d("raju", "doSomeBackgroundWork")
            }
    }

    /*
    * Using it for Light-Weight Background Tasks
    */
    fun doSomeLightWeightBackgroundWork() {
        DefaultExecutorSupplier.getInstance().forLightWeightBackgroundTasks()
            .execute {
                // do some light-weight background work here.
                Log.d("raju", "doSomeLightWeightBackgroundWork")
            }
    }

    /*
    * Using it for MainThread Tasks
    */
    fun doSomeMainThreadWork() {
        DefaultExecutorSupplier.getInstance().forMainThreadTasks()
            .execute {
                // do some Main Thread work here.
                Log.d("raju", "doSomeMainThreadWork")
            }
    }

}