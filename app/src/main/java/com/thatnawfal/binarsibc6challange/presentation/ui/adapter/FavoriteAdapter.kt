package com.thatnawfal.binarsibc6challange.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.thatnawfal.binarsibc6challange.data.local.database.FavoriteEntity
import com.thatnawfal.binarsibc6challange.databinding.ItemListFavoriteBinding

class FavoriteAdapter() : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    private var list : MutableList<FavoriteEntity> = mutableListOf()

    fun setItems(list : List<FavoriteEntity>){
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    class FavoriteViewHolder(
        private val binding: ItemListFavoriteBinding
    ) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemListFavoriteBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {

    }

    override fun getItemCount(): Int = list.size
}