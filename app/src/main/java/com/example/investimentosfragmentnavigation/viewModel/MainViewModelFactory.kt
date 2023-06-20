package com.example.investimentosfragmentnavigation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.investimentosfragmentnavigation.repository.MoedaRepository


@Suppress("UNCHECKED_CAST")
class MainViewModelFactory constructor(private val repository: MoedaRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MoedaViewModel::class.java)) {
            MoedaViewModel(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }

}