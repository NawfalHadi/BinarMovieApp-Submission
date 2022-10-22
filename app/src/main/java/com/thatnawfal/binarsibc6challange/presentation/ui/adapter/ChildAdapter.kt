package com.thatnawfal.binarsibc6challange.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.thatnawfal.binarsibc6challange.R
import com.thatnawfal.binarsibc6challange.data.network.response.MoviesListItemResponse
import com.thatnawfal.binarsibc6challange.databinding.ItemHorizontalPosterSBinding
import com.thatnawfal.binarsibc6challange.utils.Helper

class ChildAdapter(
    private val listener: itemClickListerner
) : RecyclerView.Adapter<ChildAdapter.ChildViewHolder>() {

    private var data: MutableList<MoviesListItemResponse> = mutableListOf()

    fun setItems(movies: List<MoviesListItemResponse>) {
        clearItems()
        this.data.addAll(movies)
        notifyDataSetChanged()
    }
    fun clearItems() {
        this.data.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        val binding = ItemHorizontalPosterSBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChildViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        val movie = data[position]
        holder.bindingView(movie)
    }

    override fun getItemCount(): Int = data.size

    class ChildViewHolder(
        private val binding: ItemHorizontalPosterSBinding,
        private val listener: itemClickListerner
    ) : RecyclerView.ViewHolder(binding.root){

        fun bindingView(movie: MoviesListItemResponse) {
            with(binding) {
                imgItemHorizontalPoster.load(Helper.POSTER_API_ENDPOINT + Helper.POSTER_SIZE_W185_ENDPOINT + movie.posterPath){
                    crossfade(true)
                    placeholder(R.drawable.poster_placeholder)
                }
                cvW185Poster.setOnClickListener {
                    listener.itemClicked(movie.id)
                }
            }
        }
    }
}