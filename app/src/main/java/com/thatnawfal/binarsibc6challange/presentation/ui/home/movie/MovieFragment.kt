package com.thatnawfal.binarsibc6challange.presentation.ui.home.movie

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.thatnawfal.binarsibc5challange.wrapper.Resource
import com.thatnawfal.binarsibc6challange.R
import com.thatnawfal.binarsibc6challange.databinding.FragmentMovieBinding
import com.thatnawfal.binarsibc6challange.databinding.FragmentProfileBinding
import com.thatnawfal.binarsibc6challange.presentation.logic.movie.MovieViewModel
import com.thatnawfal.binarsibc6challange.presentation.ui.adapter.ListRecycler
import com.thatnawfal.binarsibc6challange.presentation.ui.adapter.NowPlayingAdapter
import com.thatnawfal.binarsibc6challange.presentation.ui.adapter.ParentAdapter
import com.thatnawfal.binarsibc6challange.presentation.ui.adapter.itemClickListerner
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieFragment : Fragment() {

    private var _binding : FragmentMovieBinding? = null
    private val binding get() = _binding!!

    private val viewModel : MovieViewModel by viewModels()

    private val tempListData = mutableListOf<ListRecycler>()

    private val adapterListRecycler : ParentAdapter by lazy {
        ParentAdapter(object : itemClickListerner{
            override fun itemClicked(movieId: Int?) {
                movieId?.let {
                    val mBundle = Bundle()
                    mBundle.putInt("movie_id", it)
                    findNavController().navigate(R.id.action_movieFragment_to_detailMovieFragment, mBundle)
                }
            }

        })
    }

    private val adapter : NowPlayingAdapter by lazy {
        NowPlayingAdapter(object : itemClickListerner {
            override fun itemClicked(movieId: Int?) {
                movieId?.let {
                    val mBundle = Bundle()
                    mBundle.putInt("movie_id", it)
                    findNavController().navigate(R.id.action_movieFragment_to_detailMovieFragment, mBundle)
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadMovieCategories()
        observeData()
    }

    private fun loadMovieCategories() {
        viewModel.loadNowPlayingMovies()
        viewModel.loadTopRatedMovies()
        viewModel.loadPopularMovies()
    }

    private fun observeData(){
        viewModel.nowPlayingMovies.observe(requireActivity()){
            when(it){
                is Resource.Success -> {
                    it.payload?.result?.let { data -> adapter.setItems(data) }
                    initlist()
                }
            }
        }

        viewModel.topRatedListMovies.observe(requireActivity()){
            when (it) {
                is Resource.Success -> it.payload?.result?.let {
                    tempListData.add(ListRecycler(getString(R.string.toprated_movies), it))
                    viewModel.loadAllCategory.value = viewModel.loadAllCategory.value?.plus(1)
                }
            }
        }

        viewModel.popularListMovies.observe(requireActivity()){
            when (it) {
                is Resource.Success -> it.payload?.result?.let {
                    tempListData.add(ListRecycler(getString(R.string.popular_movies), it))
                    viewModel.loadAllCategory.value = viewModel.loadAllCategory.value?.plus(1)
                }
            }
        }

        viewModel.loadAllCategory.observe(requireActivity()){
            if (it == 2) {
                adapterListRecycler.setItems(tempListData)
                initRecyclerList()
            }
        }

        initRecyclerList()

    }

    private fun initlist() {
        binding.rvMovieNowPlaying.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.rvMovieNowPlaying.adapter = adapter
    }

    private fun initRecyclerList(){
        binding.rvMovieListRecycler.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.rvMovieListRecycler.adapter = adapterListRecycler
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}