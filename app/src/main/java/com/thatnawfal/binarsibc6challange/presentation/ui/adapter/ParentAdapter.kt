package com.thatnawfal.binarsibc6challange.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thatnawfal.binarsibc6challange.data.network.response.MoviesListItemResponse
import com.thatnawfal.binarsibc6challange.databinding.ItemListRecyclerBinding

class ParentAdapter(
    private val listener: itemClickListerner
) : RecyclerView.Adapter<ParentAdapter.ParentViewHolder>() {

    private var data: MutableList<ListRecycler> = mutableListOf()

    fun addItems(list: ListRecycler) {
        this.data.add(list)
        notifyDataSetChanged()
    }

    fun setItems(list: List<ListRecycler>){
        this.data.addAll(list)
    }

    fun clearItems(){
        this.data.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentViewHolder {
        val binding = ItemListRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ParentViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: ParentViewHolder, position: Int) {
        val list = data[position]
        holder.bindingView(list)
    }

    override fun getItemCount(): Int = data.size

    class ParentViewHolder(
        private val binding: ItemListRecyclerBinding,
        private val listerner: itemClickListerner
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindingView(list: ListRecycler) {
            with(binding) {
                itemTvListMovieTitle.text = list.title

                val adapter: ChildAdapter by lazy {
                    ChildAdapter(object : itemClickListerner {
                        override fun itemClicked(movieId: Int?) {
                            listerner.itemClicked(movieId)
                        }
                    })
                }

                adapter.setItems(list.listResponse)
                //adapter
                 itemRvListMovie.layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
                 itemRvListMovie.adapter = adapter
            }
        }

    }

}

data class ListRecycler(
    val title: String?,
    val listResponse: List<MoviesListItemResponse>,
    )