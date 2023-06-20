package com.example.investimentosfragmentnavigation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.investimentosfragmentnavigation.databinding.ItemMoedasBinding
import com.example.investimentosfragmentnavigation.model.MoedaModel
import com.example.investimentosfragmentnavigation.utils.FuncoesUtils.formataPorcentagem
import com.example.investimentosfragmentnavigation.utils.FuncoesUtils.trocaCorVariacaoMoeda

class ListaMoedasAdapter(
    var quandoClicaNoItem: (moeda: MoedaModel) -> Unit = {},
    moedas: List<MoedaModel?> = emptyList()
) : RecyclerView.Adapter<ListaMoedasAdapter.ViewHolder>() {

    private val moedas = moedas.toMutableList()

    class ViewHolder(
        private val binding: ItemMoedasBinding,
        private val quandoClicaNoItem: (moeda: MoedaModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var moeda: MoedaModel

        init {
            itemView.setOnClickListener {
                if (::moeda.isInitialized) {
                    quandoClicaNoItem(moeda)
                }
            }
        }

        fun vincula(moeda: MoedaModel) {

            this.moeda = moeda
            binding.txtNomeMoeda.text = moeda.isoMoeda
            moeda.variacao?.let {
                binding.txtVariacaoMoeda.text = formataPorcentagem(it)
            }
            trocaCorVariacaoMoeda(binding.txtVariacaoMoeda, moeda)
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder =
        ViewHolder(
            ItemMoedasBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            quandoClicaNoItem
        )

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {

        moedas[position]?.let {
            holder.vincula(it)
            holder.itemView.contentDescription =
                "Moeda ${position + 1}: ${it.isoMoeda}, Variação : ${it.variacao}"
        }

    }

    override fun getItemCount(): Int = moedas.size

    @SuppressLint("NotifyDataSetChanged")
    fun atualiza(moedas: List<MoedaModel?>) {
        this.moedas.clear()
        this.moedas.addAll(moedas)
        notifyDataSetChanged()
    }

}