package com.app.justimage.ui.photos

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.app.justimage.R
import com.app.justimage.data.GithubUserModel
import com.app.justimage.databinding.FragmentPhotosBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_photos.*

// needs here as well to inject fields of this classes and used when needed
@AndroidEntryPoint
class PhotosFragment : Fragment(R.layout.fragment_photos), PixabayPhotoAdapter.OnItemClickListener{

    private var firstTime: Boolean = true

    // due to AndroidEntryPoint below model will be injected by dagger
    private val viewModel by viewModels<PhotosViewModel>()

    private var _binding: FragmentPhotosBinding? = null
    private val binding get() = _binding!!

    val adapter = PixabayPhotoAdapter(this)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentPhotosBinding.bind(view)
        setupRecylerView()

        viewModel.photos.observe(viewLifecycleOwner) { data: PagingData<GithubUserModel> ->
            adapter.submitData(viewLifecycleOwner.lifecycle, data)
        }

        adapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                recyclerView.isVisible = loadState.source.refresh is LoadState.NotLoading
                if(!firstTime){
                    btn_reload.isVisible = loadState.source.refresh is LoadState.Error
                    textViewError.isVisible = loadState.source.refresh is LoadState.Error
                    firstTime = !firstTime
                }

                if (loadState.source.refresh is LoadState.NotLoading &&
                        loadState.append.endOfPaginationReached &&
                        adapter.itemCount < 1)
                {
                    recycler_view.isVisible = false
                    text_view_empty.isVisible = true
                } else {
                    textViewEmpty.isVisible = false
                }

            }
        }

        setHasOptionsMenu(true)
    }

    private fun setupRecylerView() {
        binding.apply {
            recycler_view.setHasFixedSize(true)
            recyclerView.itemAnimator = null
            recycler_view.layoutManager = GridLayoutManager(context, 1, GridLayoutManager.VERTICAL, false)
            recycler_view.adapter = adapter.withLoadStateHeaderAndFooter(
                    header = PixabayPhotoLoadStateAdapter { adapter.retry() },
                    footer = PixabayPhotoLoadStateAdapter { adapter.retry() }
            )

            btn_reload.setOnClickListener{
                adapter.retry()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_photos, menu)
        val searchItem = menu.findItem(R.id.menu_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                if (p0 != null) {
                    binding.recyclerView.scrollToPosition(0)
                    viewModel.searchPhotos(p0)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(photo: GithubUserModel) {

    }
}