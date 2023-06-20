package com.example.investimentosfragmentnavigation.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.math.BigDecimal

data class MoedaModel(
    @SerializedName("name")
    val nome: String? = null,
    @SerializedName("variation")
    var variacao: BigDecimal? = null,
    @SerializedName("buy")
    val valorCompra: Double? = null,
    @SerializedName("sell")
    val valorVenda: Double? = null,
    var isoMoeda: String = ""
) : Serializable