package com.example.investimentosfragmentnavigation.service

import com.example.investimentosfragmentnavigation.model.DataCurrencies
import retrofit2.http.GET

interface FinancasApi {
    @GET("finance?fields=only_results,currencies&key=268db62e")
    suspend fun buscaMoedas() : DataCurrencies
}