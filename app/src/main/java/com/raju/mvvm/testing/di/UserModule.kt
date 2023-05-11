package com.raju.mvvm.testing.di

import com.raju.mvvm.testing.data.UserApi
import com.raju.mvvm.testing.data.UserRepositoryImpl
import com.raju.mvvm.testing.domain.UseCase
import com.raju.mvvm.testing.domain.UserRepository
import com.raju.mvvm.testing.domain.UserUseCase
import com.raju.mvvm.testing.utils.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import retrofit2.Retrofit

@Module
@InstallIn(ViewModelComponent::class)
class UserModule {

    @Provides
    @ViewModelScoped
    fun providesUserApi(retrofit: Retrofit): UserApi {
        return retrofit.create(UserApi::class.java)
    }

    @Provides
    @ViewModelScoped
    fun providesUserRepository(userApi: UserApi): UserRepository {
        return UserRepositoryImpl(userApi)
    }

    @Provides
    @ViewModelScoped
    fun providesUseCase(
        userRepository: UserRepository,
        dispatcherProvider: DispatcherProvider
    ): UseCase {
        return UserUseCase(userRepository, dispatcherProvider)
    }
}