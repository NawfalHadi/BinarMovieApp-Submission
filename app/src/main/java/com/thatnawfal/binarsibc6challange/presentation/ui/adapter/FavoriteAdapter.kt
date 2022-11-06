package com.thatnawfal.binarsibc6challange.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.thatnawfal.binarsibc6challange.R
import com.thatnawfal.binarsibc6challange.data.local.database.FavoriteEntity
import com.thatnawfal.binarsibc6challange.databinding.ItemListFavoriteBinding
import com.thatnawfal.binarsibc6challange.utils.Helper

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

        fun bindingView(item: FavoriteEntity) {
            with(binding) {
                imgItemHorizontalPoster.load(Helper.POSTER_API_ENDPOINT + Helper.POSTER_SIZE_W185_ENDPOINT + item.poster){
                    crossfade(true)
                    placeholder(R.drawable.poster_placeholder)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemListFavoriteBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val item = list[position]
        holder.bindingView(item)
    }

    override fun getItemCount(): Int = list.size
}