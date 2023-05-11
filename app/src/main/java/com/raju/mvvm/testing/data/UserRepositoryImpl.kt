package com.raju.mvvm.testing.data

import com.raju.mvvm.testing.domain.UserRepository

class UserRepositoryImpl(private val userApi: UserApi) : UserRepository {
    override suspend fun getUsers(): List<User> = userApi.getUsers()
}