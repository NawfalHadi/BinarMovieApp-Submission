package com.thatnawfal.binarsibc6challange.presentation.ui.home.movie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.thatnawfal.binarsibc5challange.wrapper.Resource
import com.thatnawfal.binarsibc6challange.R
import com.thatnawfal.binarsibc6challange.data.network.firebase.StorageFirebase
import com.thatnawfal.binarsibc6challange.databinding.FragmentFavoriteBinding
import com.thatnawfal.binarsibc6challange.presentation.logic.movie.FavoriteViewModel
import com.thatnawfal.binarsibc6challange.presentation.ui.adapter.FavoriteAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() {

    private val storageFirebase = StorageFirebase()
    private lateinit var binding : FragmentFavoriteBinding

    private val viewModel : FavoriteViewModel by viewModels()
    private val adapter : FavoriteAdapter by lazy {
        FavoriteAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadFavoriteList()
        observeData()
    }

    private fun observeData() {
        viewModel.listFavoriteResult.observe(viewLifecycleOwner){ data ->
            adapter.setItems(data!!)
            binding.rvFavoriteItem.layoutManager = GridLayoutManager(activity, 3)
            binding.rvFavoriteItem.adapter = adapter
        }
    }

    private fun loadFavoriteList() {
        viewModel.getFavoriteNetwork()
    }
}