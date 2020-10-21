package com.example.myapplication.Paging

import androidx.paging.PagingSource
import com.example.myapplication.API.DAO.VideosDao
import com.example.myapplication.Models.VideoResponseBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException


class VideoPagingSource(private val videoDao: VideosDao) : PagingSource<Int, VideoResponseBody>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, VideoResponseBody> {
        val position = params.key ?: 0



        val result = withContext(Dispatchers.IO) {
            videoDao.fetchVideos("5", position.toString())
        }

        return try {
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