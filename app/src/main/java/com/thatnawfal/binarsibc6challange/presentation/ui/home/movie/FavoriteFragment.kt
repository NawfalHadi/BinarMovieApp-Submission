package com.thatnawfal.binarsibc6challange.presentation.ui.home.movie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.thatnawfal.binarsibc6challange.R
import com.thatnawfal.binarsibc6challange.data.network.firebase.StorageFirebase
import com.thatnawfal.binarsibc6challange.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment() {

    private val storageFirebase = StorageFirebase()
    private lateinit var binding : FragmentFavoriteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavoriteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.paidFabFirebaseSync.setOnClickListener {

        }
    }
}