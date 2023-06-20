package com.example.investimentosfragmentnavigation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.investimentosfragmentnavigation.R
import com.example.investimentosfragmentnavigation.databinding.CompravendaFragmentBinding
import com.example.investimentosfragmentnavigation.model.MoedaModel
import com.example.investimentosfragmentnavigation.utils.FuncoesUtils.formatadorMoedaBrasileiraFloat

class FragmentCompraVenda : BaseFragment() {

    private lateinit var binding: CompravendaFragmentBinding

    private lateinit var moeda: MoedaModel

    private var operacaoTexto = ""
    private var operacaoToolbar = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.compravenda_fragment, container, false)
        recebeMoeda()
        val ehOuNaoCompra = FragmentCompraVendaArgs.fromBundle(requireArguments()).ehCompra
        compraOuVenda(ehOuNaoCompra)
        configuraTela()
        configuraToolbar(
            true,
            operacaoToolbar,
            binding.toolbarCompraVenda.toolbarTitulo,
            binding.toolbarCompraVenda.btnVoltarTelaMoedas
        )
        configuraSubTituloToolbar(
            true,
            getString(R.string.cambio),
            binding.toolbarCompraVenda.toolbarSubTitulo
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configuraParaTelaHome()
        //logica de voltar ao fragment anterior está no basefragment.
//        voltaParaTelaCambio()
    }

    private fun recebeMoeda() {
        val argsMoeda = FragmentCompraVendaArgs.fromBundle(requireArguments())
        moeda = argsMoeda.moedaModel
    }

    private fun compraOuVenda(ehCompra: Boolean) {
        if (!ehCompra) {
            operacaoToolbar = getString(R.string.Vender)
            operacaoTexto = getString(R.string.vender)
        } else {
            operacaoToolbar = getString(R.string.Comprar)
            operacaoTexto = getString(R.string.comprar)
        }
    }

    //logica de voltar ao fragment anterior está no basefragment.

//    private fun voltaParaTelaCambio(){
//        binding.toolbarCompraVenda.btnVoltarTelaMoedas.setOnClickListener {
//            Navigation.findNavController(requireView()).popBackStack()
//        }
//    }

    private fun configuraTela() {
        val quantidade = FragmentCompraVendaArgs.fromBundle(requireArguments()).quantidade
        val calculoTotal = FragmentCompraVendaArgs.fromBundle(requireArguments()).total
        buildString {
            this.append(
                getString(R.string.parabens),
                getString(R.string.pula_linha),
                getString(R.string.espaco),
                getString(R.string.voce_acabou_de),
                operacaoTexto, getString(R.string.pula_linha),
                quantidade,
                getString(R.string.espaco),
                moeda?.isoMoeda,
                getString(R.string.espaco),
                getString(R.string.traco),
                getString(R.string.espaco),
                moeda?.nome,
                getString(R.string.virgula),
                getString(R.string.pula_linha),
                getString(R.string.totalizando),
                getString(R.string.pula_linha),
                formatadorMoedaBrasileiraFloat(calculoTotal)
            )
            binding.textoCompraVenda.text = this
        }
    }

    private fun configuraParaTelaHome() {
        binding.btnVaiParaHome.setOnClickListener {
            findNavController().navigate(FragmentCompraVendaDirections.acaoFragmentCompraVendaParaFragmentHome())
        }
    }
}