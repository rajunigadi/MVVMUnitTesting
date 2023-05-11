package com.raju.mvvm.testing.domain

import com.raju.mvvm.testing.data.User

interface UserRepository {
    suspend fun getUsers(): List<User>
}