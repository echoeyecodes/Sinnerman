package com.example.myapplication.Paging

import androidx.paging.PagingSource
import com.example.myapplication.API.DAO.VideosDao
import com.example.myapplication.Models.VideoResponseBody
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException


class ExploreItemPagingSource(private val videoDao: VideosDao, private val key: String, private val initialData: List<VideoResponseBody>?) : PagingSource<Int, VideoResponseBody>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, VideoResponseBody> {
        val position = params.key ?: 0

        if(position ==0 && initialData != null){
            return LoadResult.Page(
                    data = initialData,
                    prevKey = if (position == 0) null else position - 1,
                    nextKey = if (initialData.isEmpty()) null else position + initialData.size
            )
        }

        return try {
            val result = withContext(Dispatchers.IO) {
                videoDao.fetchExploreItem(key, "5", position.toString())
            }

            LoadResult.Page(
                    data = result,
                    prevKey = if (position == 0) null else position - 1,
                    nextKey = if (result.isEmpty()) null else position + result.size
            )

        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

}