package com.raju.mvvm.testing

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    UserViewModelTest::class,
    UserUseCaseTest::class,
    UserRepositoryTest::class,
)
class TestSuite