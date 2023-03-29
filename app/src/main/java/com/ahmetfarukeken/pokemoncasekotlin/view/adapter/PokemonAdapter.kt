package com.ahmetfarukeken.pokemoncasekotlin.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ahmetfarukeken.pokemoncasekotlin.databinding.PokemonRecyclerViewItemBinding
import com.ahmetfarukeken.pokemoncasekotlin.model.PokemonData
import okhttp3.internal.notify

class PokemonAdapter(val pokemonList: ArrayList<PokemonData>, val onRecyclerViewRowClickListener: OnRecyclerViewRowClickListener) :
    RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        return PokemonViewHolder(getView(parent), onRecyclerViewRowClickListener)
    }

    private fun getView(parent: ViewGroup): PokemonRecyclerViewItemBinding {
        return PokemonRecyclerViewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    }

    override fun getItemCount(): Int {
        return pokemonList.size
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        holder.bindHolder(pokemonList[position])
    }

    fun updatePokemonList(newPokemonList: ArrayList<PokemonData>) {
        pokemonList.clear()
        pokemonList.addAll(newPokemonList)
        notifyDataSetChanged()
    }

    fun addPokemonList(newPokemonList: ArrayList<PokemonData>) {
        pokemonList.addAll(newPokemonList)
        notifyDataSetChanged()
    }

    inner class PokemonViewHolder(val binding: PokemonRecyclerViewItemBinding, val onRecyclerViewRowClickListener: OnRecyclerViewRowClickListener) : RecyclerView.ViewHolder(binding.root), View.OnClickListener{
        fun bindHolder(pokemonItemData: PokemonData) {
            binding.apply {
                pokemonNameTextView.text = pokemonItemData.name
                root.setOnClickListener(this@PokemonViewHolder)
            }
        }

        override fun onClick(v: View?) {
            onRecyclerViewRowClickListener.onRowClick(adapterPosition)
        }
    }
}