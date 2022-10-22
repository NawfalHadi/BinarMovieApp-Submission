package com.thatnawfal.binarsibc6challange.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thatnawfal.binarsibc6challange.data.network.response.detailresponse.Genre
import com.thatnawfal.binarsibc6challange.databinding.ItemListGenreBinding

class GenreAdapter : RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {

    private var genres: MutableList<Genre?> = mutableListOf()

    fun setItems(genre: List<Genre?>) {
        clearItems()
        this.genres.addAll(genre)
        notifyDataSetChanged()
    }
    fun clearItems() {
        this.genres.clear()
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        val binding = ItemListGenreBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GenreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        val genre = genres.get(position)
        holder.bindingView(genre!!)
    }

    override fun getItemCount(): Int = genres?.size!!

    inner class GenreViewHolder(
        private val binding: ItemListGenreBinding
    ) : RecyclerView.ViewHolder(binding.root){
        fun bindingView(genre: Genre) {
            with(binding){
                itemgenreText.text = genre.name
            }
        }

    }
}