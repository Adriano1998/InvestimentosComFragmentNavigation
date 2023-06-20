package com.example.investimentosfragmentnavigation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.investimentosfragmentnavigation.databinding.ToolbarGenericaBinding
import com.example.investimentosfragmentnavigation.repository.MoedaRepository
import com.example.investimentosfragmentnavigation.viewModel.MainViewModelFactory
import com.example.investimentosfragmentnavigation.viewModel.MoedaViewModel

open class BaseFragment : Fragment() {

    protected val viewModel by lazy {
        ViewModelProvider(
            this,
            MainViewModelFactory(MoedaRepository())
        )[MoedaViewModel::class.java]
    }

    private val binding by lazy {
        ToolbarGenericaBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    protected open fun configuraToolbar(
        boolean: Boolean,
        titulo: String,
        txtTitulo: TextView,
        imgbtn: ImageButton
    ) {
        txtTitulo.text = titulo
        if (boolean) {
            imgbtn.visibility = View.VISIBLE
            imgbtn.setOnClickListener {
                Navigation.findNavController(requireView()).popBackStack()
            }
        }
    }

    protected open fun configuraSubTituloToolbar(
        boolean: Boolean,
        subTitulo: String,
        txtSubTitulo: TextView
    ) {
        if (boolean) {
            txtSubTitulo.text = subTitulo
            txtSubTitulo.visibility = View.VISIBLE
        }
    }
}