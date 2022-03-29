package com.app.justimage.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.app.justimage.api.PixabayApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PixabayRepository @Inject constructor(private val pixabayApi: PixabayApi) {

    fun getSearchResults(query: String) =
            Pager(
                    config = PagingConfig(20,
                            maxSize = 100, // keep it low to not eating all memory
                            enablePlaceholders = false
                    )
                    , pagingSourceFactory = { PixabayPagingSource(pixabayApi, query) }
            )
            .liveData  // turned this pager to stream so when can listen through observable methods in UI
}