package com.example.investimentosfragmentnavigation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.investimentosfragmentnavigation.R
import com.example.investimentosfragmentnavigation.adapter.ListaMoedasAdapter
import com.example.investimentosfragmentnavigation.databinding.HomeFragmentBinding
import com.example.investimentosfragmentnavigation.model.MoedaModel

class FragmentHome : BaseFragment() {

    private lateinit var binding: HomeFragmentBinding

    private val adapter by lazy {
        ListaMoedasAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.home_fragment, container, false)
        configuraToolbar(false, getString(R.string.Moedas), binding.toolbarHome.toolbarTitulo, binding.toolbarHome.btnVoltarTelaMoedas)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.listaDeMoedas.observe(viewLifecycleOwner) {
            adapter.atualiza(it)
        }
        viewModel.atualizaMoedas()

        viewModel.toastMessageObserver.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), "Erro $it", Toast.LENGTH_LONG).show()
        }
        super.onViewCreated(view, savedInstanceState)
        configuraRecyclerView()
    }

    private fun configuraRecyclerView() {
        binding.rvMoedasTelaHome.adapter = adapter
        binding.rvMoedasTelaHome.layoutManager = LinearLayoutManager(requireContext())

        adapter.quandoClicaNoItem = { moeda ->
            vaiParaTelaCambio(moeda)
        }
    }

    private fun vaiParaTelaCambio(moeda: MoedaModel) {
        findNavController().navigate(FragmentHomeDirections.acaoFragmentHomeParaFragmentCambio(moeda))
    }
}