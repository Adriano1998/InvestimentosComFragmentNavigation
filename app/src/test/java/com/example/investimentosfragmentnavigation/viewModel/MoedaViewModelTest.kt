package com.example.investimentosfragmentnavigation.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.MockKAnnotations
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

class MoedaViewModelTest{

    @get:Rule
    val rule = InstantTaskExecutorRule()
    @Before
    fun baseSetUp(){
        MockKAnnotations.init(this, relaxUnitFun = true)
    }
}