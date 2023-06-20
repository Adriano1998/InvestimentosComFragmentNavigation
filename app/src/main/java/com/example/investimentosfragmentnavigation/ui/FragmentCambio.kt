package com.example.investimentosfragmentnavigation.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.investimentosfragmentnavigation.R
import com.example.investimentosfragmentnavigation.databinding.CambioFragmentBinding
import com.example.investimentosfragmentnavigation.model.MoedaModel
import com.example.investimentosfragmentnavigation.repository.MoedaRepository
import com.example.investimentosfragmentnavigation.utils.FuncoesUtils.formataPorcentagem
import com.example.investimentosfragmentnavigation.utils.FuncoesUtils.formatadorMoedaBrasileira
import com.example.investimentosfragmentnavigation.utils.FuncoesUtils.quantidadeSaldo
import com.example.investimentosfragmentnavigation.utils.FuncoesUtils.trocaCorVariacaoMoeda
import com.example.investimentosfragmentnavigation.viewModel.MainViewModelFactory
import com.example.investimentosfragmentnavigation.viewModel.MoedaViewModel

class FragmentCambio : BaseFragment() {

    private lateinit var binding: CambioFragmentBinding

    private lateinit var moeda: MoedaModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.cambio_fragment, container, false)

        pegaArgumentos()

        moeda?.let {
            vinculaCamposMoeda(it)
            viewModel.pegaValorHashmap(it.isoMoeda)
        }

        configuraToolbar(
            true,
            getString(R.string.cambio),
            binding.toolbarCambio.toolbarTitulo,
            binding.toolbarCambio.btnVoltarTelaMoedas
        )

        configuraMostrarSaldoDisponivel()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        configuraSaldoeCaixaOnResume()
        habilitaOuDesabilitaBotaoVenda()
        habilitaOuDesabilitaBotaoCompra()

        configuraSubTituloToolbar(
            true,
            getString(R.string.Moedas),
            binding.toolbarCambio.toolbarSubTitulo
        )
    }

    private fun habilitaOuDesabilitaBotaoVenda() {
        viewModel.botaoVendaCambio.observe(viewLifecycleOwner) { botaoVendaAtivado ->
            if (botaoVendaAtivado) {
                habilitaBotao(binding.btnVender, R.drawable.retangulobotaoativado)
            } else {
                desabilitaBotao(binding.btnVender, R.drawable.retangulobotaodesativado)
            }
        }
    }

    //logica de voltar ao fragment anterior está no basefragment.

//    private fun configuraVoltaAHome() {
//        binding.toolbarCambio.btnVoltarTelaMoedas.setOnClickListener {
//            Navigation.findNavController(requireView()).popBackStack()
//        }
//    }

    private fun habilitaOuDesabilitaBotaoCompra() {
        viewModel.botaoCompraCambio.observe(viewLifecycleOwner) { botaoCompraAtivado ->
            if (botaoCompraAtivado) {
                habilitaBotao(binding.btnComprar, R.drawable.retangulobotaoativado)
            } else {
                desabilitaBotao(binding.btnComprar, R.drawable.retangulobotaodesativado)
            }
        }
    }

    private fun configuraSaldoeCaixaOnResume() {
        moeda?.let {
            buildString {
                this.append(
                    viewModel.pegaValorHashmap(it.isoMoeda).toString(),
                    getString(R.string.espaco),
                    moeda?.nome,
                    getString(R.string.espaco),
                    getString(R.string.em_caixa)
                )
                binding.txtEmCaixa.text = this
            }
        }
        binding.txtSaldoDisponivelVariavel.text = formatadorMoedaBrasileira(quantidadeSaldo)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configuraCampoQuantidade()
//        configuraVoltaAHome()
    }

    fun pegaArgumentos() {
        val args = FragmentCambioArgs.fromBundle(requireArguments())
        moeda = args.moedaModel
    }

    private fun vinculaCamposMoeda(moeda: MoedaModel) {
        buildString {
            this.append(moeda.isoMoeda, getString(R.string.espacoComTraco), moeda.nome)
            binding.txtNomeMoedaCambio.text = this
        }

        binding.txtVariacaoMoedaCambio.text =
            moeda.variacao?.let { variacao -> formataPorcentagem(variacao) }
        trocaCorVariacaoMoeda(binding.txtVariacaoMoedaCambio, moeda)
        validaCamposCompraeVenda(moeda)
    }

    private fun validaCamposCompraeVenda(it: MoedaModel) {
        if (it.valorCompra == null) {
            buildString {
                this.append(binding.txtCompraMoedaCambio.text, formatadorMoedaBrasileira(0.00))
                binding.txtCompraMoedaCambio.text = this
            }

        } else {
            buildString {
                this.append(
                    binding.txtCompraMoedaCambio.text,
                    getString(R.string.espaco),
                    moeda?.valorCompra?.let { compra -> formatadorMoedaBrasileira(compra) })
                binding.txtCompraMoedaCambio.text = this
            }
        }

        if (it.valorVenda == null) {
            buildString {
                this.append(
                    binding.txtVendaMoedaCambio.text,
                    getString(R.string.espaco),
                    formatadorMoedaBrasileira(0.00)
                )
                binding.txtVendaMoedaCambio.text = this
            }
        } else {
            buildString {
                this.append(
                    binding.txtVendaMoedaCambio.text,
                    getString(R.string.espaco),
                    moeda?.valorVenda?.let { venda -> formatadorMoedaBrasileira(venda) })
                binding.txtVendaMoedaCambio.text = this
            }
        }
    }

    private fun configuraMostrarSaldoDisponivel() {
        buildString {
            this.append(binding.txtSaldoDisponivel.text, getString(R.string.espaco))
            binding.txtSaldoDisponivel.text = this
        }
    }

    private fun configuraCampoQuantidade() {
        binding.txtinpQuantidade.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNotBlank() && moeda?.valorVenda != null) {
                    val textoDigitado = s.toString().toInt()
                    moeda?.let { moedaModel ->
                        if (viewModel.validaQuantidadeComVenda(textoDigitado, moedaModel)) {

                            viewModel.botaoVendaAtivado()

                            binding.btnVender.setOnClickListener {
                                binding.txtinpQuantidade.text?.clear()
                                val total = viewModel.calculaVenda(textoDigitado, moedaModel)
                                vaiParaTelaCompraVendaVendendo(textoDigitado, total)
                            }
                        } else {
                            viewModel.botaoVendaDesativado()
                        }
                    }
                } else {
                    viewModel.botaoVendaDesativado()
                }

                if (s.toString().isNotBlank() && moeda?.valorCompra != null) {
                    val caracteresDigitados = s.toString().toInt()
                    moeda?.let { moeda ->
                        if (viewModel.validaQuantidadeComCompra(caracteresDigitados, moeda)) {
                            viewModel.botaoCompraAtivado()

                            binding.btnComprar.setOnClickListener {
                                binding.txtinpQuantidade.text?.clear()
                                val total = viewModel.calculaCompra(caracteresDigitados, moeda)
                                vaiParaTelaCompraVendaComprando(caracteresDigitados, total)
                            }
                        } else {
                            viewModel.botaoCompraDesativado()
                        }
                    }
                } else {
                    viewModel.botaoCompraDesativado()
                }
            }
        })
    }

    private fun vaiParaTelaCompraVendaComprando(caracteresDigitados: Int, total: Float) {
        findNavController().navigate(
            FragmentCambioDirections.acaoFragmentCambioParaFragmentCompraVenda2(
                moeda,
                caracteresDigitados, total, true
            )
        )
    }

    private fun vaiParaTelaCompraVendaVendendo(textoDigitado: Int, total: Float) {
        findNavController().navigate(
            FragmentCambioDirections.acaoFragmentCambioParaFragmentCompraVenda(
                moeda, textoDigitado, total, false
            )
        )
    }

    fun habilitaBotao(botao: Button, caminhoDrawable: Int) {
        botao.isEnabled = true
        botao.setBackgroundResource(caminhoDrawable)
    }

    fun desabilitaBotao(botao: Button, caminhoDrawable: Int) {
        botao.isEnabled = false
        botao.setBackgroundResource(caminhoDrawable)
    }
}