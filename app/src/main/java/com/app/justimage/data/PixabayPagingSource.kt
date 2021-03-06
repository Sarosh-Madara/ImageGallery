package com.app.justimage.data

import androidx.paging.PagingSource
import com.app.justimage.BuildConfig
import com.app.justimage.api.PixabayApi
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1


class PixabayPagingSource(private val pixabayApi :PixabayApi,
                          private val query: String,

) : PagingSource<Int, GithubUserModel>() {

    // will trigger api request and
    // to turn data into pages
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GithubUserModel> {
        val position = params.key ?: STARTING_PAGE_INDEX

        return try {
            val response = pixabayApi.searchPhotos( query,"alphabetically",position ,params.loadSize)
            val photos = response.items

            LoadResult.Page(
                    data = photos,
                    prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                    nextKey = if (photos.isEmpty()) null else position + 1
            )
        }catch (ioException: IOException){
            LoadResult.Error(ioException)
        } catch (httpException: HttpException){
            // when server is not running
            // or when there is no data
            LoadResult.Error(httpException)
        }
    }
}