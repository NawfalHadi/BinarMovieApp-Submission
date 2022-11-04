package com.thatnawfal.binarsibc6challange.presentation.ui.home.movie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.thatnawfal.binarsibc6challange.R
import com.thatnawfal.binarsibc6challange.data.network.firebase.StorageFirebase

class FavoriteFragment : Fragment() {

    private val storageFirebase = StorageFirebase()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}