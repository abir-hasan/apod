package com.abir.hasan.apod.util

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule

@ExperimentalCoroutinesApi
open class BaseUnitTest {

    @get:Rule
    var coroutineTestRule = MainCoroutineScopeRule()

    // This rule allows the execution of live data instantly
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
}