package com.example.investimentosfragmentnavigation.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.investimentosfragmentnavigation.model.MoedaModel
import com.example.investimentosfragmentnavigation.repository.MoedaRepository
import com.example.investimentosfragmentnavigation.utils.FuncoesUtils.mapeiaNome
import com.example.investimentosfragmentnavigation.utils.FuncoesUtils.quantidadeSaldo
import com.example.investimentosfragmentnavigation.utils.FuncoesUtils.valoresMoedas
import kotlinx.coroutines.launch

class MoedaViewModel(private val repository: MoedaRepository) : BaseViewModel() {

    private val _listaDeMoedas = MutableLiveData<List<MoedaModel?>>()
    val listaDeMoedas: LiveData<List<MoedaModel?>>
        get() = _listaDeMoedas

    private val _toastMessageObserver = MutableLiveData<String>()
    val toastMessageObserver: LiveData<String>
        get() = _toastMessageObserver

    private val _botaoVendaCambio = MutableLiveData<Boolean>()
    val botaoVendaCambio: LiveData<Boolean>
        get() = _botaoVendaCambio

    private val _botaoCompraCambio = MutableLiveData<Boolean>()
    val botaoCompraCambio: LiveData<Boolean>
        get() = _botaoCompraCambio

    private val _ehCompra = MutableLiveData<Boolean>()
    val ehCompra: LiveData<Boolean>
        get() = _ehCompra

    init {
        _botaoVendaCambio.value = false
        _botaoCompraCambio.value = false
    }

    fun atualizaMoedas() {
        launch {
            try {
                val call = repository.carregaMoedas()
                val listaMoedas = mapeiaNome(
                    listOfNotNull(
                        call.currencies.USD,
                        call.currencies.EUR,
                        call.currencies.CAD,
                        call.currencies.GBP,
                        call.currencies.ARS,
                        call.currencies.AUD,
                        call.currencies.JPY,
                        call.currencies.CNY,
                        call.currencies.BTC
                    )
                )
                _listaDeMoedas.postValue(listaMoedas)
            } catch (e: Exception) {
                _toastMessageObserver.postValue("Algo inesperado aconteceu com a nossa requisição.")
            }
        }
    }

    fun botaoVendaAtivado() {
        _botaoVendaCambio.value = true
    }

    fun botaoVendaDesativado() {
        _botaoVendaCambio.value = false
    }

    fun botaoCompraAtivado() {
        _botaoCompraCambio.value = true
    }

    fun botaoCompraDesativado() {
        _botaoCompraCambio.value = false
    }

    fun validaQuantidadeComVenda(quantidade: Int, moedaModel: MoedaModel): Boolean {

        if (quantidade <= pegaValorHashmap(moedaModel.isoMoeda) && quantidade > 0) {
            return true
        }
        return false

    }

    fun validaQuantidadeComCompra(quantidade: Int, moedaModel: MoedaModel): Boolean {
        if (moedaModel.valorCompra != null) {
            if (quantidade * moedaModel.valorCompra <= quantidadeSaldo && quantidade > 0) {
                botaoCompraAtivado()
                return true
            }
            return false
        }
        return false
    }

    fun pegaValorHashmap(isoMoeda: String): Int {
        var quantidadeSimulada = 0
        if (valoresMoedas.containsKey(isoMoeda)) {
            valoresMoedas.map {
                if (it.key == isoMoeda) {
                    quantidadeSimulada = it.value
                }
            }
        }
        return quantidadeSimulada
    }


    fun calculaCompra(quantidade: Int, moedaModel: MoedaModel): Float {
        var quantidadeTotal = 0.0
        if (validaQuantidadeComCompra(quantidade, moedaModel) && moedaModel.valorCompra != null) {
            valoresMoedas.forEach {
                if (it.key == moedaModel.isoMoeda) {
                    var quantidadeSimulada = it.value
                    quantidadeSimulada += quantidade
                    valoresMoedas[it.key] = quantidadeSimulada
                }
            }
            quantidadeTotal = quantidade * moedaModel.valorCompra
            quantidadeSaldo -= quantidadeTotal
        }
        return quantidadeTotal.toFloat()
    }

    fun calculaVenda(quantidade: Int, moedaModel: MoedaModel): Float {
        var quantidadeTotal = 0.0
        if (validaQuantidadeComVenda(quantidade, moedaModel) && moedaModel.valorVenda != null) {

            valoresMoedas.forEach {
                if (it.key == moedaModel.isoMoeda) {
                    var quantidadeSimulada = it.value
                    quantidadeSimulada -= quantidade
                    valoresMoedas[it.key] = quantidadeSimulada
                }
            }
            quantidadeTotal = quantidade * moedaModel.valorVenda
            quantidadeSaldo += quantidadeTotal
        }
        return quantidadeTotal.toFloat()
    }


}