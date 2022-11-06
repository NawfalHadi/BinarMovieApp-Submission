package com.thatnawfal.binarsibc6challange.presentation.ui.home.movie

import android.annotation.SuppressLint
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.thatnawfal.binarsibc5challange.wrapper.Resource
import com.thatnawfal.binarsibc6challange.R
import com.thatnawfal.binarsibc6challange.data.network.firebase.StorageFirebase
import com.thatnawfal.binarsibc6challange.data.network.response.detailresponse.MovieDetailResponse
import com.thatnawfal.binarsibc6challange.databinding.FragmentDetailMovieBinding
import com.thatnawfal.binarsibc6challange.databinding.FragmentMovieBinding
import com.thatnawfal.binarsibc6challange.presentation.logic.movie.FavoriteViewModel
import com.thatnawfal.binarsibc6challange.presentation.logic.movie.MovieViewModel
import com.thatnawfal.binarsibc6challange.presentation.ui.adapter.ChildAdapter
import com.thatnawfal.binarsibc6challange.presentation.ui.adapter.GenreAdapter
import com.thatnawfal.binarsibc6challange.presentation.ui.adapter.itemClickListerner
import com.thatnawfal.binarsibc6challange.utils.Helper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailMovieFragment : Fragment() {

    private var _binding : FragmentDetailMovieBinding? = null
    private val binding get() = _binding!!

    private val adapterGenre : GenreAdapter by lazy {
        GenreAdapter()
    }

    private val adapterRecomended : ChildAdapter by lazy {
        ChildAdapter(object : itemClickListerner {
            override fun itemClicked(movieId: Int?) {
                findNavController().popBackStack()
                movieId?.let {
                    val mBundle = Bundle()
                    mBundle.putInt("movie_id", it)
                    findNavController().navigate(R.id.action_movieFragment_to_detailMovieFragment, mBundle)
                }
            }
        })
    }

    private val viewModel : MovieViewModel by viewModels()
    private val favoriteViewModel : FavoriteViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailMovieBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movieId = arguments?.getInt("movie_id", 0)
        binding.backButton.setOnClickListener { findNavController().popBackStack() }

        movieId?.let { viewModel.loadDetailMovie(it, getString(R.string.countryCode)) }

        favoriteViewModel.msgSnackBar.observe(viewLifecycleOwner){
            it.getContentIfNotHandled()?.let { msg ->
                Snackbar.make(
                    activity?.window?.decorView?.rootView!!,
                    msg,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        observeDetail()
    }

    private fun observeDetail() {
        viewModel.resultDetailMovie.observe(requireActivity()){
            when (it) {
                is Resource.Loading -> binding.pbDetail.isVisible = true
                is Resource.Success -> {
                    binding.pbDetail.isVisible = true
                    bindingView(it.payload)
                }

                is Resource.Empty -> {
                    binding.pbDetail.isVisible = false
                    findNavController().popBackStack()
                }

                is Resource.Error -> {
                    Log.d(ContentValues.TAG, "observeDetail: ${it.exception.toString()}")
                    binding.pbDetail.isVisible = false
                    findNavController().popBackStack()
                }
            }
        }

        viewModel.resultListRecommended.observe(requireActivity()){
            when(it){
                is Resource.Success -> {
                    binding.pbDetail.isVisible = false
                    it.payload?.result?.let {
                        adapterRecomended.setItems(it)
                        binding.rvDetailRecomList.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
                        binding.rvDetailRecomList.adapter = adapterRecomended
                    }
                }

                is Resource.Empty -> {
                    binding.pbDetail.isVisible = false
                }

                is Resource.Error -> {
                    Log.d(ContentValues.TAG, "observeDetail: ${it.exception.toString()}")
                    binding.pbDetail.isVisible = false
                    findNavController().popBackStack()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun bindingView(movie: MovieDetailResponse?) {
        movie?.genres?.let { adapterGenre.setItems(it) }

        binding.ivDetailBackdropImg.load(Helper.POSTER_API_ENDPOINT + Helper.POSTER_SIZE_W780_ENDPOINT + movie?.backdropPath) {
            crossfade(true)
            placeholder(R.drawable.poster_placeholder)
        }

        binding.ivDetailPoster.load(Helper.POSTER_API_ENDPOINT + Helper.POSTER_SIZE_W780_ENDPOINT + movie?.posterPath) {
            crossfade(true)
            placeholder(R.drawable.poster_placeholder)
        }

        binding.fabDetailFavorite.isVisible = false
        favoriteViewModel.checkFavoriteNetwork(movie?.id!!)
        favoriteViewModel.isFavorited.observe(viewLifecycleOwner){
            binding.fabDetailFavorite.isVisible = true
            if (it){
                binding.fabDetailFavorite.setOnClickListener {
                    favoriteViewModel.deleteFavoriteNetwork(movie.id)
                }
            } else {
                binding.fabDetailFavorite.setOnClickListener {
                    favoriteViewModel.addFavoriteNetwork(movie.id ?: 0, "1xHa7jfkL", movie.posterPath.toString())
                    favoriteViewModel.newFavorite.observe(viewLifecycleOwner){

                    }
                }
            }
        }



        movie?.let {
            it.id?.let { movies -> viewModel.loadRecommendedMovies(movies) }
            binding.tvDetailTitle.text = "${it.title}"
            binding.tvDetailOverview.text = "${it.overview}"
            binding.tvDetailRuntime.text = " : ${it.runtime.toString()} ${getString(R.string.minute)}"
            binding.tvDetailStatus.text = " : ${it.status}"
            binding.tvDetailReleaseDate.text = " : ${it.releaseDate}"
        }

        binding.rvDetailGenreList.layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvDetailGenreList.adapter = adapterGenre
    }

}