package com.app.justimage.ui.photos

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.app.justimage.data.PixabayRepository

class PhotosViewModel @ViewModelInject constructor(
        private val repository: PixabayRepository,
        @Assisted state: SavedStateHandle)
    : ViewModel(){

    private val currentQuery = state.getLiveData(CURRENT_QUERY, DEFAULT_QUERY)

    val photos = currentQuery.switchMap { queryString ->
        repository.getSearchResults(queryString).cachedIn(viewModelScope)
    }

    fun searchPhotos(query: String){
        currentQuery.value = query
    }

    companion object{
        private const val CURRENT_QUERY : String = "current_query"
        private const val DEFAULT_QUERY : String = ""
    }
}