package com.example.investimentosfragmentnavigation.repository

import com.example.investimentosfragmentnavigation.model.DataCurrencies
import com.example.investimentosfragmentnavigation.service.RetrofitHelper


class MoedaRepository {

    val services = RetrofitHelper().initApiFinancas()
    suspend fun carregaMoedas(): DataCurrencies {
        return services.buscaMoedas()
    }
}